public static void main(String[] args) {
        // Define the current date
        LocalDate currentDate = LocalDate.now();

        // Define date formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        // Calculate the start and end dates for the last four weeks
        LocalDate week1End = currentDate;
        LocalDate week1Start = week1End.minusDays(6);
        LocalDate week2End = week1Start.minusDays(1);
        LocalDate week2Start = week2End.minusDays(6);
        LocalDate week3End = week2Start.minusDays(1);
        LocalDate week3Start = week3End.minusDays(6);
        LocalDate week4End = week3Start.minusDays(1);
        LocalDate week4Start = week4End.minusDays(6);

        // Calculate the overall date range
        LocalDate overallStart = week4Start;
        LocalDate overallEnd = week1End;

        // Print the results
        System.out.println("Last four weeks date: " + overallStart.format(formatter) + " to " + overallEnd.format(formatter));
        System.out.println("Week1: " + week1Start.format(formatter) + " to " + week1End.format(formatter));
        System.out.println("Week2: " + week2Start.format(formatter) + " to " + week2End.format(formatter));
        System.out.println("Week3: " + week3Start.format(formatter) + " to " + week3End.format(formatter));
        System.out.println("Week4: " + week4Start.format(formatter) + " to " + week4End.format(formatter));
    }
