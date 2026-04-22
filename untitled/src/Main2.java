class DevLibraryWindow {
    // All fields are private and final
    private final String title;
    private final boolean hasSidebar;
    private final boolean hasSearchIcon;
    private final String theme;

    // The Giant Constructor
    private DevLibraryWindow(Builder builder) {
        this.title = builder.title;
        this.hasSidebar = builder.hasSidebar;
        this.hasSearchIcon = builder.hasSearchIcon;
        this.theme = builder.theme;
    }

    public void display() {
        System.out.println("Displaying " + title + " [Theme: " + theme + ", Sidebar: " + hasSidebar + ", Search: " + hasSearchIcon + "]");
    }

    public static class Builder{
        private final String title;
        private  boolean hasSidebar;
        private  boolean hasSearchIcon;
        private  String theme;

        public Builder(String title) {
            this.title = title;
        }
        public Builder addhasSidebar(){
            this.hasSearchIcon=true;
            return this;
        }
        public Builder addhasSearchIcon(){
            this.hasSearchIcon=true;
            return this;
        }
        public Builder addtheme(String theme) {
            this.theme = theme;
            return this;
        }
        public DevLibraryWindow build(){
            return new DevLibraryWindow(this);
        }


    }
}

public class Main2 {
    public static void main(String[] args) {
        // Building a simple window (Forced to type 'false' and default values manually)
        DevLibraryWindow loginWindow = new DevLibraryWindow.Builder("lies of b")
                .addhasSearchIcon()
                .addhasSearchIcon()
                .addtheme("black")
                .build();


        // Building a complex main window
        DevLibraryWindow loginWindow2 = new DevLibraryWindow.Builder("lies of b")
                .addhasSearchIcon()
                .addhasSearchIcon()
                .addtheme("white")
                .build();
        loginWindow.display();
        loginWindow2.display();
    }
}