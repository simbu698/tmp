public static void main(String[] args) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Get the current week start and end dates (Sunday to Saturday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date startOfCurrentWeek = calendar.getTime();
        calendar.add(Calendar.DAY_OF_WEEK, 6); // Add 6 days to get Saturday
        Date endOfCurrentWeek = calendar.getTime();

        // Print the current week
        System.out.println("Current Week (" + dateFormat.format(startOfCurrentWeek) + " to " + dateFormat.format(endOfCurrentWeek) + ")");

        // Get the last four weeks, including 5th to 11th January
        Calendar weekCalendar = Calendar.getInstance();
        weekCalendar.setTime(currentDate);

        // Adjust to include the week 5th to 11th January
        weekCalendar.add(Calendar.WEEK_OF_YEAR, -1); // Go back one week to include 5th-11th Jan
        weekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // Start from Sunday
        Date startOfWeek1 = weekCalendar.getTime();
        weekCalendar.add(Calendar.DAY_OF_WEEK, 6); // Add 6 days to get Saturday
        Date endOfWeek1 = weekCalendar.getTime();
        
        // Print the adjusted Week 1
        System.out.println("Week1: " + dateFormat.format(startOfWeek1) + " to " + dateFormat.format(endOfWeek1));

        // Print the last three weeks (Week2 to Week4)
        for (int i = 2; i <= 4; i++) {
            weekCalendar.add(Calendar.WEEK_OF_YEAR, -1); // Move to the previous week
            weekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Date startOfWeek = weekCalendar.getTime();
            weekCalendar.add(Calendar.DAY_OF_WEEK, 6); // Add 6 days to get Saturday
            Date endOfWeek = weekCalendar.getTime();
            
            System.out.println("Week" + i + ": " + dateFormat.format(startOfWeek) + " to " + dateFormat.format(endOfWeek));
        }
    }
