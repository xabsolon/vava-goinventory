# GoInventory 

## Launching release build

### Prerequisites
- Make sure you have Java installed
- You will also need [JavaFX](https://openjfx.io/openjfx-docs/#install-javafx)
- Install MysSQL database and run it locally as a service

1. Create a DB user named ```goinventory``` with password ```password```.

2. Create a database by the following the SQL script in the directory ```sql/goInventorySchema.sql```.

3. The following command will launch the application
    ```
    java -jar --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml vava_goinventory.jar
   ```
