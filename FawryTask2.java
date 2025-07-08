import java.util.*;

enum BookType {
    Paperbook,
    EBook,
    Showcase_Demo_Book
}

class Book {
    BookType type;
    String isbn;
    String title;
    int year;
    double price;
    int stock = 0;         // only for paper books
    String filetype = "";  // only for ebooks

    public Book(BookType type, String isbn, String title, int year, double price, int stock, String filetype) {
        this.type = type;
        this.isbn = isbn;
        this.title = title;
        this.year = year;
        this.price = price;
        this.stock = stock;
        this.filetype = filetype;
    }
}

class BookStore {
    private List<Book> inventory = new ArrayList<>();

    public void addBook(BookType type, String isbn, String title, int year, double price, int quantity, String filetype) {
        boolean found = false;

        if (type == BookType.Paperbook) {
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.get(i).isbn.equals(isbn)) {
                    inventory.get(i).stock += quantity;
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            int stock = (type == BookType.Paperbook) ? quantity : 0;
            Book book = new Book(type, isbn, title, year, price, stock, filetype);
            inventory.add(book);
        }

        System.out.println("Book ISBN: " + isbn + " added successfully!");
    }

    public void removeBooks(int currentYear, int period) {
        Iterator<Book> it = inventory.iterator();
        while (it.hasNext()) {
            Book b = it.next();
            if (currentYear - b.year > period) {
                System.out.println("Book \"" + b.title + "\" (ISBN: " + b.isbn + ") has been removed as it is outdated.");
                it.remove();
            }
        }
    }

    public void buyBook(String isbn, int quantity, String email, String address) {
        boolean found = false;
        for (int i = 0; i < inventory.size(); i++) {
            Book b = inventory.get(i);
            if (b.isbn.equals(isbn)) {
                found = true;
                switch (b.type) {
                    case Paperbook:
                        if (b.stock < quantity) {
                            System.out.println("Quantity is not available. Only " + b.stock + " in stock.");
                        } else {
                            b.stock -= quantity;
                            System.out.println("Paid amount: $" + (b.price * quantity));
                            shippingService(b, address);
                            if (b.stock == 0) {
                                inventory.remove(i);
                            }
                        }
                        break;
                    case EBook:
                        System.out.println("Paid amount: $" + (b.price * quantity));
                        mailingService(b, email);
                        break;
                    case Showcase_Demo_Book:
                        System.out.println("This book is not for sale.");
                        break;
                    default:
                        System.out.println("Unknown book type.");
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Book with ISBN " + isbn + " is not available in the bookstore.");
        }
    }

    private void shippingService(Book b, String address) {
        System.out.println("Book sent to shipping service at: " + address);
    }

    private void mailingService(Book b, String email) {
        System.out.println("Book sent to mailing service to: " + email);
    }
}

class BookStoreTester {
    public static void runTests() {
        BookStore store = new BookStore();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        System.out.println("\n--- TEST: Add Books ---");
        store.addBook(BookType.Paperbook, "111", "Macbeth", 2015, 150.0, 5, "");
        store.addBook(BookType.EBook, "222", "Learn Java", 2020, 90.0, 0, "PDF");
        store.addBook(BookType.Showcase_Demo_Book, "333", "Geography", 2010, 0.0, 0, "");

        System.out.println("\n--- TEST: Buy Book (valid quantity) ---");
        store.buyBook("111", 2, "user1@example.com", "Fifth Settlement");

        System.out.println("\n--- TEST: Buy Book (exceeds stock) ---");
        store.buyBook("111", 10, "user2@example.com", "Rehab City");

        System.out.println("\n--- TEST: Buy EBook ---");
        store.buyBook("222", 1, "user3@example.com", "Maadi");

        System.out.println("\n--- TEST: Buy Showcase Book ---");
        store.buyBook("333", 1, "user4@example.com", "Dokki");

        System.out.println("\n--- TEST: Remove Outdated Books (older than 8 years) ---");
        store.removeBooks(currentYear, 8);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookStoreTester.runTests();

        System.out.print("\nDo you want to manually use the system? (y/n): ");
        String input = scanner.next();

        if (input.equalsIgnoreCase("y")) {
            BookStore store = new BookStore();
            int choice=0;
        while(choice!=4) {
                System.out.println("\n--- Welcome to the book store! ---");
                System.out.println("1. Add a book");
                System.out.println("2. Remove outdated books");
                System.out.println("3. Buy a book");
                System.out.println("4. Exit store");
                choice = scanner.nextInt();
                scanner.nextLine(); // clear newline

                switch (choice) {
                    case 1:
                        System.out.println("-- Add a Book --");
                        System.out.println("1. Paper Book\n2. EBook\n3. Showcase/Demo Book");
                        int typeNum = scanner.nextInt();
                        scanner.nextLine();
                        BookType type = BookType.values()[typeNum - 1];

                        System.out.print("Book ISBN: ");
                        String isbn = scanner.nextLine();
                        System.out.print("Book Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Year Published: ");
                        int year = scanner.nextInt();
                        System.out.print("Book Price: ");
                        double price = scanner.nextDouble();
                        int quantity = 0;
                        String filetype = "";

                        if (type == BookType.Paperbook) {
                            System.out.print("Quantity: ");
                            quantity = scanner.nextInt();
                        } else if (type == BookType.EBook) {
                            scanner.nextLine(); // clear
                            System.out.print("Ebook File Type: ");
                            filetype = scanner.nextLine();
                        }

                        store.addBook(type, isbn, title, year, price, quantity, filetype);
                        break;

                    case 2:
                        System.out.print("Enter the max age (in years) for books: ");
                        int period = scanner.nextInt();
                        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                        store.removeBooks(currentYear, period);
                        break;

                    case 3:
                        System.out.print("Book ISBN: ");
                        String buyIsbn = scanner.nextLine();
                        System.out.print("Quantity: ");
                        int buyQty = scanner.nextInt();
                        scanner.nextLine(); // clear
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Address: ");
                        String address = scanner.nextLine();
                        store.buyBook(buyIsbn, buyQty, email, address);
                        break;

                    case 4:
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } 
        }

        scanner.close();
    }
}
