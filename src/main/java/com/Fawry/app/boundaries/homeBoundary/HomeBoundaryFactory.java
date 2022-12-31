package com.Fawry.app.boundaries.homeBoundary;

import com.Fawry.app.models.User;

public class HomeBoundaryFactory {
    public HomeBoundary getHomeBoundary(User user) throws Exception {

        if (user.isAdmin()) {
            return new AdminHomeBoundary(user);
        }
        return new NormalUserHomeBoundary(user);
    }
}
