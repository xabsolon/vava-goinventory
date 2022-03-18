# GoInventory 

## Running a development build

1. Go to src/main/resources
2. Duplicate system.properties.example file and rename it to system.properties
3. Enter database credentials inside system.properties file
4. Click the run button in the IntelliJ IDEA

Note: If you get an error saying table.records class could not be found, run `maven install` script

## Logging while coding

Since we have to log most of the business logic and errors/exceptions
We should be trying to implement meaningful exceptions as well as log them.
Behind every catch block should be represented as follows:

``` 
catch (IOException e) {
            log.Exceptions("Failed to load login screen",e);
```
With implemented function in log class 

```
public void Exceptions(String type, Exception e){
        LOGGER.log(Level.SEVERE, type + ", " + e);
    }
```