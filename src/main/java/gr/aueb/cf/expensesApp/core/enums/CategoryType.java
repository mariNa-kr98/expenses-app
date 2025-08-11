package gr.aueb.cf.expensesApp.core.enums;

import java.util.List;

public enum CategoryType {

    INCOME("salary", "tips", "income", "bonus"),
    EXPENSE("super_market", "phone_bill", "internet", "electricity",
                "water_bill", "parking", "gas", "health", "entertainment",
                "gym", "savings", "vehicle_insurance", "vehicle_registration_fee",
                "vehicle_inspection");

    private final List<String> subcategories;

    CategoryType(String... subcategories) {
        this.subcategories = List.of(subcategories);
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    @Override
    public String toString() {
        return "CategoryType{" +
                "subcategories=" + subcategories +
                '}';
    }
}
