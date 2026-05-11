const currentUser = "Student";

// Frontend files are served from src/main/resources/static.
// Later, replace the dummy array below with backend API calls using relative URLs.
// Use fetch("/api/resources"), not full localhost URLs.
// The frontend must only communicate with backend APIs and must never connect directly to the database.
//
// Future API connection points:
// GET /api/resources
// GET /api/resources/{id}
// POST /api/add
// PUT /api/update/{id}
// DELETE /api/delete/{id}
// POST /api/upload
// GET /api/download/{id}
let resources = [
    {
        id: "B-1001",
        resourceType: "book",
        title: "Introduction to Java",
        courseName: "CPIT252",
        description: "A useful Java book for OOP concepts.",
        referenceType: "link",
        url: "https://example.com/java-book",
        fileName: "",
        uploadedBy: "Ahmed",
        author: "John Smith",
        weekNumber: "",
        noteType: "",
        projectLanguage: "",
        projectType: ""
    },
    {
        id: "B-1002",
        resourceType: "book",
        title: "Database Systems Essentials",
        courseName: "CPIT240",
        description: "Clear chapters about ER diagrams, SQL, and normalization.",
        referenceType: "file",
        url: "",
        fileName: "database-systems-essentials.pdf",
        uploadedBy: "Student",
        author: "Linda Brown",
        weekNumber: "",
        noteType: "",
        projectLanguage: "",
        projectType: ""
    },
    {
        id: "S-1003",
        resourceType: "slides",
        title: "Facade Pattern Slides",
        courseName: "CPIT252",
        description: "Lecture slides explaining the facade design pattern with examples.",
        referenceType: "file",
        url: "",
        fileName: "facade-pattern-week6.pdf",
        uploadedBy: "Sara",
        author: "",
        weekNumber: "6",
        noteType: "",
        projectLanguage: "",
        projectType: ""
    },
    {
        id: "S-1004",
        resourceType: "slides",
        title: "Web Security Overview",
        courseName: "CPIT405",
        description: "Slides covering authentication, authorization, and common web risks.",
        referenceType: "link",
        url: "https://example.com/security-slides",
        fileName: "",
        uploadedBy: "Student",
        author: "",
        weekNumber: "9",
        noteType: "",
        projectLanguage: "",
        projectType: ""
    },
    {
        id: "N-1005",
        resourceType: "notes",
        title: "Spring Boot Exam Notes",
        courseName: "CPIT252",
        description: "Concise notes for controllers, services, repositories, and dependency injection.",
        referenceType: "file",
        url: "",
        fileName: "spring-boot-exam-notes.docx",
        uploadedBy: "Hossam",
        author: "",
        weekNumber: "",
        noteType: "Exam Review",
        projectLanguage: "",
        projectType: ""
    },
    {
        id: "N-1006",
        resourceType: "notes",
        title: "Algorithms Cheat Sheet",
        courseName: "CPIT305",
        description: "Big O, sorting algorithms, graphs, and dynamic programming summary.",
        referenceType: "link",
        url: "https://example.com/algorithms-notes",
        fileName: "",
        uploadedBy: "Student",
        author: "",
        weekNumber: "",
        noteType: "Cheat Sheet",
        projectLanguage: "",
        projectType: ""
    },
    {
        id: "P-1007",
        resourceType: "project",
        title: "Library Management Project",
        courseName: "CPIT251",
        description: "Sample project structure for a course library management system.",
        referenceType: "file",
        url: "",
        fileName: "library-management-project.zip",
        uploadedBy: "Ahmed",
        author: "",
        weekNumber: "",
        noteType: "",
        projectLanguage: "Java",
        projectType: "Desktop App"
    },
    {
        id: "P-1008",
        resourceType: "project",
        title: "Student Portal Demo",
        courseName: "CPIT405",
        description: "Simple web project demo with pages for students and admins.",
        referenceType: "link",
        url: "https://example.com/student-portal-demo",
        fileName: "",
        uploadedBy: "Student",
        author: "",
        weekNumber: "",
        noteType: "",
        projectLanguage: "JavaScript",
        projectType: "Web App"
    }
];

let selectedResourceId = null;
let editingResourceId = null;
let lastBrowseResourceIds = [];

const pageIds = ["homePage", "browsePage", "formPage", "detailsPage", "uploadsPage"];
const typeLabels = {
    book: "Books",
    slides: "Slides",
    notes: "Notes",
    project: "Projects"
};

document.addEventListener("DOMContentLoaded", () => {
    setupNavigation();
    setupFilters();
    setupForm();
    setupCardActions();
    renderAll();
    showPage("homePage");
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
        document.getElementById(containerId).addEventListener("click", (event) => {
            const button = event.target.closest("[data-action]");
            if (!button) {
                return;
            }

            const resourceId = button.dataset.id;
            const action = button.dataset.action;

            if (action === "view") {
                openDetails(resourceId);
            } else if (action === "edit") {
                openEditForm(resourceId);
            } else if (action === "delete") {
                deleteResource(resourceId);
            } else if (action === "open") {
                openResourceLink(resourceId);
            } else if (action === "download") {
                downloadResource(resourceId);
            } else if (action === "back") {
                showPage("browsePage");
            }
        });
    });
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
    const counts = {
        total: resources.length,
        book: countByType("book"),
        slides: countByType("slides"),
        notes: countByType("notes"),
        project: countByType("project")
    };

    const cards = [
        { label: "Total Resources", value: counts.total },
        { label: "Books", value: counts.book },
        { label: "Slides", value: counts.slides },
        { label: "Notes", value: counts.notes },
        { label: "Projects", value: counts.project }
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
    document.getElementById("recentResources").innerHTML = recent
        .map((resource) => createResourceCard(resource, { compact: true }))
        .join("");
}

function renderBrowseResources() {
    const searchValue = document.getElementById("searchInput").value.trim().toLowerCase();
    const typeValue = document.getElementById("typeFilter").value;
    const referenceValue = document.getElementById("referenceFilter").value;

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
    const myResources = resources.filter((resource) => resource.uploadedBy === currentUser);
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
    document.getElementById("fileStatus").textContent = resource.fileName
        ? `Current file: ${resource.fileName}`
        : "";

    updateDynamicFields();
    updateReferenceFields();
    showPage("formPage");
}

function saveResource(event) {
    event.preventDefault();

    const referenceType = getValue("referenceType");
    const existingResource = editingResourceId ? findResource(editingResourceId) : null;
    const fileInput = document.getElementById("fileInput");
    const selectedFileName = fileInput.files.length > 0 ? fileInput.files[0].name : "";
    const fileName = referenceType === "file"
        ? selectedFileName || (existingResource ? existingResource.fileName : "")
        : "";
    const url = referenceType === "link" ? getValue("url") : "";

    if (referenceType === "link" && !url) {
        showMessage("Please enter a URL for link resources.", "error");
        return;
    }

    if (referenceType === "file" && !fileName) {
        showMessage("Please choose a file for file resources.", "error");
        return;
    }

    const resourceData = normalizeResource({
        id: editingResourceId || createResourceId(getValue("resourceType")),
        resourceType: getValue("resourceType"),
        title: getValue("title"),
        courseName: getValue("courseName"),
        description: getValue("description"),
        uploadedBy: existingResource ? existingResource.uploadedBy : currentUser,
        referenceType,
        url,
        fileName,
        author: getValue("author"),
        weekNumber: getValue("weekNumber"),
        noteType: getValue("noteType"),
        projectLanguage: getValue("projectLanguage"),
        projectType: getValue("projectType")
    });

    if (editingResourceId) {
        const index = resources.findIndex((resource) => resource.id === editingResourceId);
        resources[index] = resourceData;
        selectedResourceId = resourceData.id;
        editingResourceId = null;
        clearResourceForm();
        renderAll();
        showMessage("Resource updated successfully.", "success");
        showPage("browsePage");
    } else {
        resources.push(resourceData);
        clearResourceForm();
        renderAll();
        showMessage("Resource added successfully.", "success");
        showPage("browsePage");
    }
}

function deleteResource(resourceId) {
    const resource = findResource(resourceId);
    if (!resource) {
        showMessage("Resource was not found.", "error");
        return;
    }

    const confirmed = window.confirm(`Delete "${resource.title}"?`);
    if (!confirmed) {
        return;
    }

    resources = resources.filter((item) => item.id !== resourceId);
    if (selectedResourceId === resourceId) {
        selectedResourceId = null;
    }
    renderAll();
    showMessage("Resource deleted.", "success");

    if (document.getElementById("detailsPage").classList.contains("active-page")) {
        showPage("browsePage");
    }
}

function openDetails(resourceId) {
    selectedResourceId = resourceId;
    renderDetails();
    showPage("detailsPage");
}

function renderDetails() {
    const detailsContainer = document.getElementById("resourceDetails");
    const resource = findResource(selectedResourceId) || resources.find((item) => lastBrowseResourceIds.includes(item.id)) || resources[0];

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

    selectedResourceId = resource.id;
    const optionalMeta = getOptionalMeta(resource);
    const referenceInfo = resource.referenceType === "link"
        ? `<p><strong>URL:</strong> <a class="preview-url" href="${escapeAttribute(resource.url)}" target="_blank" rel="noopener noreferrer">${escapeHtml(resource.url)}</a></p>`
        : `<p><strong>File Name:</strong> ${escapeHtml(resource.fileName)}</p>`;
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
                <p><strong>${escapeHtml(resource.fileName)}</strong></p>
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
    document.getElementById("linkFields").classList.toggle("hidden", referenceType !== "link");
    document.getElementById("fileFields").classList.toggle("hidden", referenceType !== "file");
    document.getElementById("url").required = referenceType === "link";
}

function clearResourceForm() {
    document.getElementById("resourceForm").reset();
    document.getElementById("resourceFormTitle").textContent = "Add Resource";
    document.getElementById("saveResourceBtn").textContent = "Save Resource";
    document.getElementById("fileStatus").textContent = "";
    updateDynamicFields();
    updateReferenceFields();
}

function normalizeResource(resource) {
    const normalized = {
        id: resource.id,
        resourceType: resource.resourceType,
        title: resource.title,
        courseName: resource.courseName,
        description: resource.description,
        uploadedBy: resource.uploadedBy,
        referenceType: resource.referenceType,
        url: resource.url,
        fileName: resource.fileName,
        author: "",
        weekNumber: "",
        noteType: "",
        projectLanguage: "",
        projectType: ""
    };

    if (resource.resourceType === "book") {
        normalized.author = resource.author;
    } else if (resource.resourceType === "slides") {
        normalized.weekNumber = resource.weekNumber;
    } else if (resource.resourceType === "notes") {
        normalized.noteType = resource.noteType;
    } else if (resource.resourceType === "project") {
        normalized.projectLanguage = resource.projectLanguage;
        normalized.projectType = resource.projectType;
    }

    return normalized;
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

function downloadResource(resourceId) {
    const resource = findResource(resourceId);
    if (!resource || resource.referenceType !== "file") {
        showMessage("No file is available for this resource.", "error");
        return;
    }
    showMessage(`Download will be connected later. Dummy file: ${resource.fileName}`, "success");
}

function showMessage(message, type = "success") {
    const messageBox = document.getElementById("appMessage");
    messageBox.textContent = message;
    messageBox.className = `app-message visible ${type}`;
    window.clearTimeout(showMessage.timeoutId);
    showMessage.timeoutId = window.setTimeout(() => {
        messageBox.className = "app-message";
        messageBox.textContent = "";
    }, 3200);
}

function countByType(type) {
    return resources.filter((resource) => resource.resourceType === type).length;
}

function findResource(resourceId) {
    return resources.find((resource) => resource.id === resourceId);
}

function createResourceId(resourceType) {
    const prefix = resourceType.charAt(0).toUpperCase();
    const nextNumber = resources.length + 1001;
    let id = `${prefix}-${nextNumber}`;
    while (findResource(id)) {
        id = `${prefix}-${Number(id.split("-")[1]) + 1}`;
    }
    return id;
}

function getValue(id) {
    return document.getElementById(id).value.trim();
}

function setValue(id, value) {
    document.getElementById(id).value = value || "";
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
