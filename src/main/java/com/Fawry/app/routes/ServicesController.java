package com.Fawry.app.routes;

import com.Fawry.app.custom.Response;
import com.Fawry.app.helperClasses.Services;
import com.Fawry.app.models.Service;
import com.Fawry.app.models.ServicesData;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/services")
public class ServicesController {

    ServicesData servicesData;
    Services services;

    ServicesController() throws SQLException {
        servicesData = new ServicesData();
        services = new Services();
    }

    @GetMapping("/")
    public List<Service> index() {
        return services.getAllServices();
    }

    @GetMapping("/search")
    public List<Service> search(@RequestParam(value = "q") String query) {
        return services.searchServices(query);
    }


}
