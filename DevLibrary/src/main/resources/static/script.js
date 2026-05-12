let resources = [];

let currentUser = "";

const API = {
    resources: "/api/resources",
    resource: (id) => `/api/resources/${encodeURIComponent(id)}`,
    add: "/api/add",
    update: (id) => `/api/update/${encodeURIComponent(id)}`,
    delete: (id) => `/api/delete/${encodeURIComponent(id)}`,
    upload: "/api/upload",
    download: (id) => `/api/download/${encodeURIComponent(id)}`,
    currentUser: "/api/current-user"
};

let selectedResourceId = null;
let editingResourceId = null;
let lastBrowseResourceIds = [];

const pageIds = ["homePage", "browsePage", "formPage", "detailsPage", "uploadsPage"];

document.addEventListener("DOMContentLoaded", async () => {
    setupNavigation();
    setupFilters();
    setupForm();
    setupCardActions();
    setupProfileMenu();
    renderAll();
    showPage("homePage");
    await loadCurrentUser();
    await loadResources();
});

function setupNavigation() {
    document.querySelectorAll("[data-page]").forEach((button) => {
        button.addEventListener("click", () => {
            const pageId = button.dataset.page;
            if (pageId === "formPage" && button.dataset.mode === "add") {
                openAddForm();
                return;
            }
            showPage(pageId);
        });
    });
}

function setupFilters() {
    ["searchInput", "typeFilter", "referenceFilter"].forEach((id) => {
        document.getElementById(id).addEventListener("input", renderBrowseResources);
    });

    document.getElementById("clearFiltersBtn").addEventListener("click", () => {
        document.getElementById("filterForm").reset();
        renderBrowseResources();
    });
}

function setupForm() {
    document.getElementById("resourceType").addEventListener("change", updateDynamicFields);
    document.getElementById("referenceType").addEventListener("change", updateReferenceFields);
    document.getElementById("resourceForm").addEventListener("submit", saveResource);
    document.getElementById("clearFormBtn").addEventListener("click", () => {
        clearResourceForm();
        showMessage("Form cleared.", "success");
    });
    document.getElementById("cancelFormBtn").addEventListener("click", () => {
        const destination = editingResourceId ? "browsePage" : "homePage";
        editingResourceId = null;
        clearResourceForm();
        showPage(destination);
    });
}

function setupCardActions() {
    ["browseResources", "recentResources", "myUploads", "resourceDetails"].forEach((containerId) => {
        document.getElementById(containerId).addEventListener("click", async (event) => {
            const button = event.target.closest("[data-action]");
            if (!button) {
                return;
            }

            const resourceId = button.dataset.id;
            const action = button.dataset.action;

            if (action === "view") {
                await openDetails(resourceId);
            } else if (action === "edit") {
                openEditForm(resourceId);
            } else if (action === "delete") {
                await deleteResource(resourceId);
            } else if (action === "open") {
                openResourceLink(resourceId);
            } else if (action === "download") {
                await downloadResource(resourceId);
            } else if (action === "back") {
                showPage("browsePage");
            }
        });
    });
}

function setupProfileMenu() {
    const profileButton = document.getElementById("profileButton");
    const profileDropdown = document.getElementById("profileDropdown");
    if (!profileButton || !profileDropdown) {
        return;
    }

    updateProfileName();
    profileButton.addEventListener("click", (event) => {
        event.stopPropagation();
        const isOpen = profileDropdown.classList.toggle("hidden");
        profileButton.setAttribute("aria-expanded", String(!isOpen));
    });

    document.addEventListener("click", () => {
        profileDropdown.classList.add("hidden");
        profileButton.setAttribute("aria-expanded", "false");
    });
}

function updateProfileName() {
    const profileButton = document.getElementById("profileButton");
    if (profileButton) {
        profileButton.textContent = currentUser || "User";
    }
}

async function loadResources() {
    try {
        const data = await requestJson(API.resources, {
            method: "GET",
            errorMessage: "Failed to load resources"
        });
        resources = Array.isArray(data) ? data.map(normalizeResourceFromApi) : [];
        renderAll();
        if (document.getElementById("detailsPage").classList.contains("active-page")) {
            renderDetails();
        }
    } catch (error) {
        console.error("Failed to load resources", error);
        showMessage("Failed to load resources.", "error");
    }
}

async function loadCurrentUser() {
    try {
        const data = await requestJson(API.currentUser, {
            method: "GET",
            errorMessage: "Failed to load current user"
        });
        if (data?.username) {
            currentUser = data.username;
            updateProfileName();
        }
    } catch (error) {
        console.error("Failed to load current user", error);
    }
}

async function requestJson(url, options = {}) {
    const { errorMessage = "Request failed", ...fetchOptions } = options;
    const response = await fetch(url, {
        credentials: "same-origin",
        ...fetchOptions,
        headers: {
            Accept: "application/json",
            ...(fetchOptions.headers || {})
        }
    });

    if (!response.ok) {
        const responseText = await readResponseText(response);
        throw new Error(`${errorMessage}. Status ${response.status}. ${responseText}`);
    }

    if (response.status === 204) {
        return null;
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

async function requestWithoutJson(url, options = {}) {
    const { errorMessage = "Request failed", ...fetchOptions } = options;
    const response = await fetch(url, {
        credentials: "same-origin",
        ...fetchOptions
    });

    if (!response.ok) {
        const responseText = await readResponseText(response);
        throw new Error(`${errorMessage}. Status ${response.status}. ${responseText}`);
    }

    return response;
}

async function readResponseText(response) {
    try {
        return await response.text();
    } catch (error) {
        return "";
    }
}

function showPage(pageId) {
    pageIds.forEach((id) => {
        document.getElementById(id).classList.toggle("active-page", id === pageId);
    });

    document.querySelectorAll(".nav-menu .nav-link, .brand").forEach((link) => {
        const linkPage = link.dataset.page;
        const active = linkPage === pageId || (pageId === "detailsPage" && linkPage === "browsePage");
        link.classList.toggle("active", active);
    });

    if (pageId === "homePage") {
        renderHome();
    } else if (pageId === "browsePage") {
        renderBrowseResources();
    } else if (pageId === "uploadsPage") {
        renderMyUploads();
    } else if (pageId === "detailsPage") {
        renderDetails();
    }

    window.scrollTo({ top: 0, behavior: "smooth" });
}

function renderAll() {
    renderHome();
    renderBrowseResources();
    renderMyUploads();
}

function renderHome() {
    renderDashboard();
    renderRecentResources();
}

function renderDashboard() {
    const cards = [
        { label: "Total Resources", value: resources.length },
        { label: "Books", value: countByType("book") },
        { label: "Slides", value: countByType("slides") },
        { label: "Notes", value: countByType("notes") },
        { label: "Projects", value: countByType("project") }
    ];

    document.getElementById("dashboardCards").innerHTML = cards.map((card) => `
        <article class="dashboard-card">
            <span>${escapeHtml(card.label)}</span>
            <strong>${card.value}</strong>
        </article>
    `).join("");
}

function renderRecentResources() {
    const recent = resources.slice(-4).reverse();
    document.getElementById("recentResources").innerHTML = recent.length
        ? recent.map((resource) => createResourceCard(resource, { compact: true })).join("")
        : `<p class="empty-state visible">No resources have been added yet.</p>`;
}

function renderBrowseResources() {
    const searchValue = getValue("searchInput").toLowerCase();
    const typeValue = getValue("typeFilter");
    const referenceValue = getValue("referenceFilter");

    const filteredResources = resources.filter((resource) => {
        const matchesSearch = !searchValue
            || resource.title.toLowerCase().includes(searchValue)
            || resource.courseName.toLowerCase().includes(searchValue);
        const matchesType = typeValue === "all" || resource.resourceType === typeValue;
        const matchesReference = referenceValue === "all" || resource.referenceType === referenceValue;
        return matchesSearch && matchesType && matchesReference;
    });

    lastBrowseResourceIds = filteredResources.map((resource) => resource.id);
    document.getElementById("browseResources").innerHTML = filteredResources.map(createResourceCard).join("");
    document.getElementById("browseEmpty").classList.toggle("visible", filteredResources.length === 0);
}

function renderMyUploads() {
    const myResources = currentUser
        ? resources.filter((resource) => resource.uploadedBy === currentUser)
        : [];
    document.getElementById("myUploads").innerHTML = myResources
        .map((resource) => createResourceCard(resource, { limitedActions: true }))
        .join("");
    document.getElementById("uploadsEmpty").classList.toggle("visible", myResources.length === 0);
}

function createResourceCard(resource, options = {}) {
    const optionalMeta = getOptionalMeta(resource);
    const actionButtons = options.limitedActions
        ? `
            <button class="btn btn-small btn-primary" type="button" data-action="view" data-id="${escapeAttribute(resource.id)}">View</button>
            <button class="btn btn-small btn-secondary" type="button" data-action="edit" data-id="${escapeAttribute(resource.id)}">Edit</button>
            <button class="btn btn-small btn-danger" type="button" data-action="delete" data-id="${escapeAttribute(resource.id)}">Delete</button>
        `
        : createFullActionButtons(resource, options.compact);

    return `
        <article class="resource-card">
            <div class="card-top">
                <h3>${escapeHtml(resource.title)}</h3>
                <div class="badge-row">
                    <span class="badge">${escapeHtml(resource.resourceType)}</span>
                    <span class="badge ${escapeAttribute(resource.referenceType)}">${escapeHtml(resource.referenceType)}</span>
                </div>
            </div>
            <p class="muted">${escapeHtml(resource.description)}</p>
            <div class="meta-list">
                <p><strong>Course:</strong> ${escapeHtml(resource.courseName)}</p>
                <p><strong>Uploaded by:</strong> ${escapeHtml(resource.uploadedBy)}</p>
                ${optionalMeta.map((item) => `<p><strong>${escapeHtml(item.label)}:</strong> ${escapeHtml(item.value)}</p>`).join("")}
            </div>
            <div class="card-actions">${actionButtons}</div>
        </article>
    `;
}

function createFullActionButtons(resource, compact = false) {
    const sizeClass = compact ? "btn-small" : "";
    const referenceButton = resource.referenceType === "link"
        ? `<button class="btn ${sizeClass} btn-light" type="button" data-action="open" data-id="${escapeAttribute(resource.id)}">Open Link</button>`
        : `<button class="btn ${sizeClass} btn-light" type="button" data-action="download" data-id="${escapeAttribute(resource.id)}">Download</button>`;

    return `
        <button class="btn ${sizeClass} btn-primary" type="button" data-action="view" data-id="${escapeAttribute(resource.id)}">View</button>
        <button class="btn ${sizeClass} btn-secondary" type="button" data-action="edit" data-id="${escapeAttribute(resource.id)}">Edit</button>
        <button class="btn ${sizeClass} btn-danger" type="button" data-action="delete" data-id="${escapeAttribute(resource.id)}">Delete</button>
        ${referenceButton}
    `;
}

function openAddForm() {
    editingResourceId = null;
    clearResourceForm();
    document.getElementById("resourceFormTitle").textContent = "Add Resource";
    document.getElementById("saveResourceBtn").textContent = "Save Resource";
    showPage("formPage");
}

function openEditForm(resourceId) {
    const resource = findResource(resourceId);
    if (!resource) {
        showMessage("Resource was not found.", "error");
        return;
    }

    editingResourceId = resourceId;
    document.getElementById("resourceFormTitle").textContent = "Edit Resource";
    document.getElementById("saveResourceBtn").textContent = "Save Changes";

    setValue("resourceType", resource.resourceType);
    setValue("title", resource.title);
    setValue("courseName", resource.courseName);
    setValue("description", resource.description);
    setValue("referenceType", resource.referenceType);
    setValue("url", resource.url);
    setValue("author", resource.author);
    setValue("weekNumber", resource.weekNumber);
    setValue("noteType", resource.noteType);
    setValue("projectLanguage", resource.projectLanguage);
    setValue("projectType", resource.projectType);
    document.getElementById("fileInput").value = "";
    document.getElementById("fileStatus").textContent = resource.referenceType === "file" && resource.fileName
        ? `Current file: ${resource.fileName}. File replacement is not available through the update endpoint yet.`
        : "";

    updateDynamicFields();
    updateReferenceFields();
    showPage("formPage");
}

async function saveResource(event) {
    event.preventDefault();

    const referenceType = getValue("referenceType");
    const existingResource = editingResourceId ? findResource(editingResourceId) : null;

    if (editingResourceId) {
        await updateResource(existingResource, referenceType);
    } else if (referenceType === "file") {
        await uploadResource();
    } else {
        await addLinkResource();
    }
}

async function addLinkResource() {
    const payload = buildJsonPayload({ includeUrl: true });

    if (!payload.url) {
        showMessage("Please enter a URL for link resources.", "error");
        return;
    }

    try {
        await requestJson(API.add, {
            method: "POST",
            errorMessage: "Failed to add resource",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        clearResourceForm();
        showMessage("Resource added successfully.", "success");
        await loadResources();
        showPage("browsePage");
    } catch (error) {
        console.error("Failed to add resource", error);
        showMessage("Failed to add resource.", "error");
    }
}

async function uploadResource() {
    const fileInput = document.getElementById("fileInput");
    if (!fileInput.files.length) {
        showMessage("Please choose a file to upload.", "error");
        return;
    }

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);
    appendFormDataValue(formData, "resourceType", getValue("resourceType"));
    appendFormDataValue(formData, "title", getValue("title"));
    appendFormDataValue(formData, "courseName", getValue("courseName"));
    appendFormDataValue(formData, "description", getValue("description"));
    appendFormDataValue(formData, "author", getValue("author"));
    appendFormDataValue(formData, "weekNumber", getValue("weekNumber"));
    appendFormDataValue(formData, "noteType", getValue("noteType"));
    appendFormDataValue(formData, "projectLanguage", getValue("projectLanguage"));
    appendFormDataValue(formData, "projectType", getValue("projectType"));

    try {
        await requestWithoutJson(API.upload, {
            method: "POST",
            errorMessage: "Failed to upload file",
            body: formData
        });

        clearResourceForm();
        showMessage("File resource uploaded successfully.", "success");
        await loadResources();
        showPage("browsePage");
    } catch (error) {
        console.error("Failed to upload file", error);
        showMessage("Failed to upload file.", "error");
    }
}

async function updateResource(existingResource, referenceType) {
    if (!existingResource) {
        showMessage("Resource was not found.", "error");
        return;
    }

    const fileInput = document.getElementById("fileInput");
    if (referenceType === "file" && existingResource.referenceType !== "file") {
        showMessage("Changing a link resource to a file requires adding a new file resource.", "error");
        return;
    }
    if (referenceType === "file" && fileInput.files.length > 0) {
        showMessage("Replacing files while editing is not supported by the current update endpoint.", "error");
        return;
    }

    const payload = buildJsonPayload({ includeUrl: referenceType === "link" });

    if (referenceType === "link" && !payload.url) {
        showMessage("Please enter a URL for link resources.", "error");
        return;
    }

    try {
        await requestJson(API.update(existingResource.id), {
            method: "PUT",
            errorMessage: "Failed to update resource",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        selectedResourceId = existingResource.id;
        editingResourceId = null;
        clearResourceForm();
        showMessage("Resource updated successfully.", "success");
        await loadResources();
        showPage("browsePage");
    } catch (error) {
        console.error("Failed to update resource", error);
        showMessage("Failed to update resource.", "error");
    }
}

function buildJsonPayload(options = {}) {
    const payload = {
        resourceType: getValue("resourceType"),
        title: getValue("title"),
        courseName: getValue("courseName"),
        description: getValue("description")
    };

    if (options.includeUrl) {
        payload.url = getValue("url");
    }

    const resourceType = payload.resourceType;
    if (resourceType === "book") {
        addIfPresent(payload, "author", getValue("author"));
    } else if (resourceType === "slides") {
        const weekNumber = getValue("weekNumber");
        if (weekNumber) {
            payload.weekNumber = Number(weekNumber);
        }
    } else if (resourceType === "notes") {
        addIfPresent(payload, "noteType", getValue("noteType"));
    } else if (resourceType === "project") {
        addIfPresent(payload, "projectLanguage", getValue("projectLanguage"));
        addIfPresent(payload, "projectType", getValue("projectType"));
    }

    return payload;
}

async function deleteResource(resourceId) {
    const resource = findResource(resourceId);
    if (!resource) {
        showMessage("Resource was not found.", "error");
        return;
    }

    const confirmed = window.confirm("Are you sure you want to delete this resource?");
    if (!confirmed) {
        return;
    }

    try {
        await requestWithoutJson(API.delete(resourceId), {
            method: "DELETE",
            errorMessage: "Failed to delete resource"
        });

        if (selectedResourceId === resourceId) {
            selectedResourceId = null;
        }

        showMessage("Resource deleted.", "success");
        await loadResources();

        if (document.getElementById("detailsPage").classList.contains("active-page")) {
            showPage("browsePage");
        }
    } catch (error) {
        console.error("Failed to delete resource", error);
        showMessage("Failed to delete resource.", "error");
    }
}

async function openDetails(resourceId) {
    selectedResourceId = resourceId;
    let resource = findResource(resourceId);

    if (!resource) {
        try {
            const data = await requestJson(API.resource(resourceId), {
                method: "GET",
                errorMessage: "Failed to load resource details"
            });
            resource = normalizeResourceFromApi(data);
            upsertResource(resource);
        } catch (error) {
            console.error("Failed to load resource details", error);
            showMessage("Failed to load resource details.", "error");
            return;
        }
    }

    renderDetails();
    showPage("detailsPage");
}

function renderDetails() {
    const detailsContainer = document.getElementById("resourceDetails");
    const resource = findResource(selectedResourceId);

    if (!resource) {
        detailsContainer.innerHTML = `
            <div class="details-panel">
                <h1>Resource not found</h1>
                <p class="muted">There is no resource to preview.</p>
                <button class="btn btn-primary" type="button" data-action="back">Back to Browse</button>
            </div>
        `;
        return;
    }

    const optionalMeta = getOptionalMeta(resource);
    const referenceInfo = resource.referenceType === "link"
        ? `<p><strong>URL:</strong> <a class="preview-url" href="${escapeAttribute(resource.url)}" target="_blank" rel="noopener noreferrer">${escapeHtml(resource.url)}</a></p>`
        : `<p><strong>File Name:</strong> ${escapeHtml(resource.fileName || "File resource")}</p>`;
    const previewBox = resource.referenceType === "link"
        ? `
            <div class="preview-box">
                <h3>Link Preview</h3>
                <p class="preview-url">${escapeHtml(resource.url)}</p>
                <button class="btn btn-primary" type="button" data-action="open" data-id="${escapeAttribute(resource.id)}">Open Link</button>
            </div>
        `
        : `
            <div class="preview-box">
                <h3>File Preview</h3>
                <p><strong>${escapeHtml(resource.fileName || "File resource")}</strong></p>
                <p class="muted">File preview will be connected later.</p>
                <button class="btn btn-primary" type="button" data-action="download" data-id="${escapeAttribute(resource.id)}">Download</button>
            </div>
        `;

    detailsContainer.innerHTML = `
        <article class="details-panel">
            <div class="details-header">
                <div>
                    <p class="eyebrow">Resource Preview</p>
                    <h1>${escapeHtml(resource.title)}</h1>
                </div>
                <div class="badge-row">
                    <span class="badge">${escapeHtml(resource.resourceType)}</span>
                    <span class="badge ${escapeAttribute(resource.referenceType)}">${escapeHtml(resource.referenceType)}</span>
                </div>
            </div>
            <p class="muted">${escapeHtml(resource.description)}</p>
            <div class="detail-list">
                <p><strong>ID:</strong> ${escapeHtml(resource.id)}</p>
                <p><strong>Resource Type:</strong> ${escapeHtml(resource.resourceType)}</p>
                <p><strong>Course Name:</strong> ${escapeHtml(resource.courseName)}</p>
                <p><strong>Uploaded By:</strong> ${escapeHtml(resource.uploadedBy)}</p>
                <p><strong>Reference Type:</strong> ${escapeHtml(resource.referenceType)}</p>
                ${referenceInfo}
                ${optionalMeta.map((item) => `<p><strong>${escapeHtml(item.label)}:</strong> ${escapeHtml(item.value)}</p>`).join("")}
            </div>
            ${previewBox}
            <div class="details-actions">
                <button class="btn btn-secondary" type="button" data-action="back">Back to Browse</button>
                <button class="btn btn-primary" type="button" data-action="edit" data-id="${escapeAttribute(resource.id)}">Edit Resource</button>
                <button class="btn btn-danger" type="button" data-action="delete" data-id="${escapeAttribute(resource.id)}">Delete Resource</button>
            </div>
        </article>
    `;
}

function updateDynamicFields() {
    const type = getValue("resourceType");
    ["bookFields", "slidesFields", "notesFields", "projectFields"].forEach((id) => {
        document.getElementById(id).classList.add("hidden");
    });
    document.getElementById(`${type}Fields`).classList.remove("hidden");
}

function updateReferenceFields() {
    const referenceType = getValue("referenceType");
    const editingResource = editingResourceId ? findResource(editingResourceId) : null;
    const editingFile = editingResource?.referenceType === "file" && referenceType === "file";
    document.getElementById("linkFields").classList.toggle("hidden", referenceType !== "link");
    document.getElementById("fileFields").classList.toggle("hidden", referenceType !== "file");
    document.getElementById("url").required = referenceType === "link";
    document.getElementById("fileInput").required = referenceType === "file" && !editingFile;
}

function clearResourceForm() {
    document.getElementById("resourceForm").reset();
    document.getElementById("resourceFormTitle").textContent = "Add Resource";
    document.getElementById("saveResourceBtn").textContent = "Save Resource";
    document.getElementById("fileStatus").textContent = "";
    updateDynamicFields();
    updateReferenceFields();
}

function normalizeResourceFromApi(resource) {
    const reference = stringValue(resource?.reference);
    const resourceType = normalizeResourceType(resource?.resourceType);
    const referenceType = detectReferenceType(resource, reference);
    const url = referenceType === "link" ? stringValue(resource?.url || reference) : "";
    const fileName = referenceType === "file"
        ? stringValue(resource?.fileName || extractFileName(reference))
        : "";

    return {
        id: stringValue(resource?.id),
        resourceType,
        title: stringValue(resource?.title),
        courseName: stringValue(resource?.courseName),
        description: stringValue(resource?.description),
        uploadedBy: stringValue(resource?.uploadedBy),
        reference,
        referenceType,
        url,
        fileName,
        author: stringValue(resource?.author),
        weekNumber: resource?.weekNumber ?? "",
        noteType: stringValue(resource?.noteType),
        projectLanguage: stringValue(resource?.projectLanguage),
        projectType: stringValue(resource?.projectType)
    };
}

function detectReferenceType(resource, reference) {
    const explicitType = stringValue(resource?.referenceType).toLowerCase();
    if (explicitType === "link" || explicitType === "file") {
        return explicitType;
    }
    if (resource?.url || /^https?:\/\//i.test(reference)) {
        return "link";
    }
    return "file";
}

function normalizeResourceType(type) {
    const lowerType = stringValue(type).toLowerCase();
    if (lowerType === "slide") {
        return "slides";
    }
    if (lowerType === "note") {
        return "notes";
    }
    return lowerType || "book";
}

function extractFileName(reference) {
    const cleanReference = stringValue(reference).split("|")[0].trim();
    const parts = cleanReference.split(/[\\/]/);
    return parts[parts.length - 1] || cleanReference;
}

function getOptionalMeta(resource) {
    const meta = [];
    if (resource.author) {
        meta.push({ label: "Author", value: resource.author });
    }
    if (resource.weekNumber) {
        meta.push({ label: "Week Number", value: resource.weekNumber });
    }
    if (resource.noteType) {
        meta.push({ label: "Note Type", value: resource.noteType });
    }
    if (resource.projectLanguage) {
        meta.push({ label: "Project Language", value: resource.projectLanguage });
    }
    if (resource.projectType) {
        meta.push({ label: "Project Type", value: resource.projectType });
    }
    return meta;
}

function openResourceLink(resourceId) {
    const resource = findResource(resourceId);
    if (!resource || resource.referenceType !== "link" || !resource.url) {
        showMessage("No link is available for this resource.", "error");
        return;
    }
    window.open(resource.url, "_blank", "noopener,noreferrer");
}

async function downloadResource(resourceId) {
    const resource = findResource(resourceId);
    if (!resource || resource.referenceType !== "file") {
        showMessage("No file is available for this resource.", "error");
        return;
    }

    try {
        const response = await requestWithoutJson(API.download(resourceId), {
            method: "GET",
            errorMessage: "Failed to download file"
        });
        const blob = await response.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = downloadUrl;
        link.download = getDownloadFileName(response, resource);
        document.body.appendChild(link);
        link.click();
        link.remove();
        window.URL.revokeObjectURL(downloadUrl);
    } catch (error) {
        console.error("Failed to download file", error);
        showMessage("Failed to download file.", "error");
    }
}

function getDownloadFileName(response, resource) {
    const disposition = response.headers.get("Content-Disposition") || "";
    const match = disposition.match(/filename="?([^"]+)"?/i);
    return match ? match[1] : resource.fileName || "resource-download";
}

function showMessage(message, type = "success") {
    const messageBox = document.getElementById("appMessage");
    messageBox.textContent = message;
    messageBox.className = `app-message visible ${type}`;
    window.clearTimeout(showMessage.timeoutId);
    showMessage.timeoutId = window.setTimeout(() => {
        messageBox.className = "app-message";
        messageBox.textContent = "";
    }, 3800);
}

function countByType(type) {
    return resources.filter((resource) => resource.resourceType === type).length;
}

function findResource(resourceId) {
    return resources.find((resource) => resource.id === resourceId);
}

function upsertResource(resource) {
    const index = resources.findIndex((item) => item.id === resource.id);
    if (index >= 0) {
        resources[index] = resource;
    } else {
        resources.push(resource);
    }
}

function appendFormDataValue(formData, name, value) {
    if (value !== "") {
        formData.append(name, value);
    }
}

function addIfPresent(payload, key, value) {
    if (value !== "") {
        payload[key] = value;
    }
}

function getValue(id) {
    return document.getElementById(id).value.trim();
}

function setValue(id, value) {
    document.getElementById(id).value = value ?? "";
}

function stringValue(value) {
    return value == null ? "" : String(value);
}

function escapeHtml(value) {
    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

function escapeAttribute(value) {
    return escapeHtml(value);
}
