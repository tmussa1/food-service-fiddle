package cscie55.hw5.reader;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import cscie55.hw5.foodservice.Dish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class MenuFileReader {

    private static final Logger logger = LogManager.getLogger(MenuFileReader.class.getName());

    public List<Dish> read(String fileName){

        ObjectMapper mapper = new ObjectMapper();
        logger.info("Read from file");

        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", fileName);
        File f = new File(path.toString());
        List<Dish> menu = null;
        if(f.exists()){
            String content = null;
            try {
                content = new String(Files.readAllBytes(f.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Dish[] obj = mapper.readValue(new File(path.toString()), Dish[].class);
                menu = Arrays.asList(obj);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return menu;

    }
}
