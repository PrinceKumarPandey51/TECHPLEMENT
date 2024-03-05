import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

 public class currencyconverter {

    private static final String API_URL = "https://api.exchangerate.host/latest";
    private static final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final Map<String, Double> exchangeRates = new HashMap<>();
    private static final Map<String, String> favorites = new HashMap<>();

    public static void main(String[] args) {
        try {
            fetchExchangeRates();
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch exchange rates: " + e.getMessage());
            return;
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1-> Convert currency");
            System.out.println("2-> Add favorite currency");
            System.out.println("3-> View favorite currencies");
            System.out.println("4-> Update favorite currency");
            System.out.println("5-> Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    convertCurrency(sc);
                    break;
                case 2:
                    addFavoriteCurrency(sc);
                    break;
                case 3:
                    viewFavoriteCurrencies();
                    break;
                case 4:
                    updateFavoriteCurrency(sc);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void fetchExchangeRates() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String body = response.body();
            exchangeRates.put("EUR",0.89);
            exchangeRates.put("INR", 82.91);
            exchangeRates.put("USD", 1.23);
        } else {
            System.err.println("Failed to fetch exchange rates: HTTP " + response.statusCode());
        }
    }

    private static void convertCurrency(Scanner sc) {
        System.out.print("Enter the amount to convert: ");
        double amount = sc.nextDouble();

        System.out.print("Enter the source currency (e.g., USD): ");
        String sourceCurrency = sc.next().toUpperCase();

        System.out.print("Enter the target currency (e.g., EUR): ");
        String targetCurrency = sc.next().toUpperCase();

        if (!exchangeRates.containsKey(sourceCurrency) || !exchangeRates.containsKey(targetCurrency)) {
            System.out.println("Invalid currencies.");
            return;
        }

        double rate = exchangeRates.get(targetCurrency)/(exchangeRates.get(sourceCurrency));
        double convertedAmount = amount * rate;

        System.out.println(amount + " " + sourceCurrency + " is equivalent to " + convertedAmount + " " + targetCurrency);
    }

    private static void addFavoriteCurrency(Scanner sc) {
        System.out.print("Enter the currency code to add to favorites: ");
        String currencyCode = sc.next().toUpperCase();

        if (exchangeRates.containsKey(currencyCode)) {
            favorites.put(currencyCode, currencyCode);
            System.out.println(currencyCode + " added to favorites.");
        } else {
            System.out.println("Invalid currency code.");
        }
    }

    private static void viewFavoriteCurrencies() {
        System.out.println("Favorite currencies:");
        favorites.keySet().forEach(currencyCode -> System.out.println(currencyCode));
    }

    private static void updateFavoriteCurrency(Scanner sc) {
        System.out.print("Enter the currency code to update: ");
        String currencyCode = sc.next().toUpperCase();

        if (favorites.containsKey(currencyCode)) {
            favorites.remove(currencyCode);
            addFavoriteCurrency(sc);
        } else {
            System.out.println("Currency code not found in favorites.");
        }
    }
}
