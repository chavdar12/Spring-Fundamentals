package bg.softuni.mobilele.service;

import bg.softuni.mobilele.model.view.StatsView;

public interface StatsService {
    void onRequest();
    StatsView getStats();
}
