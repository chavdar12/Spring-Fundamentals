package bg.softuni.coffeeshop.model;

public enum CategoryName {
    COFFEE(2), CAKE(10), DRINK(1), OTHER(5);

    private final int neededTime;

    CategoryName(int neededTime) {
        this.neededTime = neededTime;
    }

    public int getNeededTime() {
        return neededTime;
    }
}
