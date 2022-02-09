package ru.gb.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.gb.config.ShopConfig;
import ru.gb.entity.Manufacturer;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ShopConfig.class)
class ManufacturerDaoTest {

    @Autowired
    ManufacturerDao manufacturerDao;

    @Test
    public void saveTest() {
        Manufacturer manufacturer = Manufacturer.builder()
                .name("Tesla")
                .build();

        Manufacturer savedManufacturer = manufacturerDao.save(manufacturer);

        assertAll(
                () -> assertEquals(1L, savedManufacturer.getId()),
                () -> assertEquals("Tesla", savedManufacturer.getName()),
                () -> assertEquals(0, savedManufacturer.getVersion()),
                () -> assertEquals("User", savedManufacturer.getCreatedBy(), "mustn't be null"),
                () -> assertEquals("User", savedManufacturer.getLastModifiedBy(), "mustn't be null"),
                () -> assertNotNull(savedManufacturer.getCreatedDate(), "mustn't be null"),
                () -> assertNotNull(savedManufacturer.getLastModifiedDate(), "mustn't be null")
        );
    }

}