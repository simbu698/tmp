public static void main(String[] args) {
        // Current date
        LocalDate currentDate = LocalDate.now(); // Today is January 8th, 2025

        // Find the start of the current week (Sunday)
        int daysToSubtract = currentDate.getDayOfWeek().getValue() % 7; // Get days to previous Sunday
        LocalDate startOfWeek = currentDate.minusDays(daysToSubtract);

        // Print the last four weeks
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        for (int i = 0; i < 4; i++) {
            // Calculate the end of the current week (Saturday)
            LocalDate weekEnd = startOfWeek.plusDays(6);  // Saturday of the current week
            LocalDate weekStart = weekEnd.minusDays(6);   // Sunday of the current week

            // Print week range
            System.out.println("Week" + (4 - i) + ": " + weekStart.format(formatter) + " to " + weekEnd.format(formatter));

            // Move to the previous week
            startOfWeek = startOfWeek.minusWeeks(1);
        }
    }
