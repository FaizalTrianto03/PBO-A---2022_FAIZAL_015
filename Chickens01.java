public class Chickens01 {
    public static void main(String[] args) {
        int eggsPerChicken1 = 5;
        int chickenCount1 = 3;
        int totalEggs1 = 0;
        double eggsMonday = eggsPerChicken1 * chickenCount1;
        totalEggs1 += eggsMonday;
        chickenCount1++;
        double eggsTuesday = eggsPerChicken1 * chickenCount1;
        totalEggs1 += eggsTuesday;
        chickenCount1 /= 2;
        double eggsWednesday = eggsPerChicken1 * chickenCount1;
        totalEggs1 += eggsWednesday;



        System.out.println("Total telur yang terkumpul dari hari senin hingga rabu adalah: " + totalEggs1 + " telur") ;

        int eggsPerChicken2 = 4;
        int chickenCount2 = 8;
        int totalEggs2 = 0;
        double eggsMonday2 = eggsPerChicken2 * chickenCount2;
        totalEggs2 += eggsMonday2;
        chickenCount2++;
        double eggsTuesday2 = eggsPerChicken2 * chickenCount2;
        totalEggs2 += eggsTuesday2;
        chickenCount2 /= 2;
        double eggsWednesday2 = eggsPerChicken2 * chickenCount2;
        totalEggs2 += eggsWednesday2;



        System.out.println("Total telur yang terkumpul dari hari senin hingga rabu adalah: " + totalEggs2 + " telur");
    }
}
