package com.Fawry.app.helperClasses.formAndHandler;

import com.Fawry.app.models.Service;

public class Form {

    private Service service;
    private Handler handler;

    public Form(Service service, Handler handler) {
        this.service = service;
        this.handler = handler;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
