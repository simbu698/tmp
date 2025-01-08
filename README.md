public class DateRangExample {
    public static void printLastFourWeeksDateRange() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate endOfWeek = today.minusDays(today.getDayOfWeek().getValue() % 7);
        LocalDate startOfWeek = endOfWeek.minusDays(6);
        String overallStart = startOfWeek.minusWeeks(3).format(formatter);
        String overallEnd = endOfWeek.format(formatter);
        System.out.println("Last four weeks date: " + overallStart + " to " + overallEnd);
        for (int i = 1; i <= 4; i++) {
            System.out.println("Week" + i + ": " + startOfWeek.format(formatter)
                    + " to " + endOfWeek.format(formatter));
            endOfWeek = endOfWeek.minusWeeks(1); // Move to the previous week
            startOfWeek = endOfWeek.minusDays(6);
        }
    }

    public static void main(String[] args) {
        printLastFourWeeksDateRange();
    }
}
