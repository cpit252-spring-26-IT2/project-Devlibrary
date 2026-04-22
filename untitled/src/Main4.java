// 1. The Products (These stay exactly the same)
interface Document {
    void open();
    void save();
}

class WordDocument implements Document {
    public void open() { System.out.println("Opening Word Document..."); }
    public void save() { System.out.println("Saving Word Document..."); }
}

class PdfDocument implements Document {
    public void open() { System.out.println("Opening PDF Document..."); }
    public void save() { System.out.println("Saving PDF Document..."); }
}

// 2. The Creator (Now an Abstract Class)
abstract class DocumentProcessor {

    // THE FACTORY METHOD: It promises to return a Document, but leaves the details to the subclasses.
    public abstract Document createDocument();

    // The core business logic. It no longer needs a String parameter!
    // It just calls the factory method to get a document, then processes it.
    public void processDocument() {
        Document doc = createDocument();
        doc.open();
        doc.save();
    }
}

// 3. The Concrete Creators (The Subclasses)
class WordProcessor extends DocumentProcessor {
    @Override
    public Document createDocument() {
        return new WordDocument(); // This child specifically builds Word documents
    }
}

class PdfProcessor extends DocumentProcessor {
    @Override
    public Document createDocument() {
        return new PdfDocument(); // This child specifically builds PDF documents
    }
}

public class Main4 {
    public static void main(String[] args) {
        // We want to process a Word document, so we use the WordProcessor
        DocumentProcessor wordApp = new WordProcessor();
        wordApp.processDocument();

        // We want to process a PDF document, so we use the PdfProcessor
        DocumentProcessor pdfApp = new PdfProcessor();
        pdfApp.processDocument();
    }
}