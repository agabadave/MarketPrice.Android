package ug.co.dave.marketprice.entities;

/**
 * Created by dave on 12/19/2014.
 */
public class Commodity extends BaseEntity {

    private String name;

    private String description;

    private String unitOfSale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitOfSale() {
        return unitOfSale;
    }

    public void setUnitOfSale(String unitOfSale) {
        this.unitOfSale = unitOfSale;
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
