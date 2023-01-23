package rumstajn.parfem.parfem.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;

@Entity(tableName = "perfume")
public class Perfume {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String manufacturer;
    private PerfumeGenderType gender;
    private Date productionDate;
    private String imagePath;

    public Perfume(@NonNull String name, @NonNull String manufacturer, @NonNull PerfumeGenderType gender,
                   @NonNull Date productionDate, @NonNull String imagePath) {

        this.name = name;
        this.manufacturer = manufacturer;
        this.gender = gender;
        this.productionDate = productionDate;
        this.imagePath = imagePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public PerfumeGenderType getGender() {
        return gender;
    }

    public void setGender(PerfumeGenderType gender) {
        this.gender = gender;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
