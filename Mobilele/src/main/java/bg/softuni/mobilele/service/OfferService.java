package bg.softuni.mobilele.service;

import bg.softuni.mobilele.model.binding.OfferAddBindModel;
import bg.softuni.mobilele.model.service.OfferAddServiceModel;
import bg.softuni.mobilele.model.service.OfferUpdateServiceModel;
import bg.softuni.mobilele.model.view.OfferDetailsView;
import bg.softuni.mobilele.model.view.OfferSummaryView;

import java.util.List;

public interface OfferService {
    void initializeOffers();

    List<OfferSummaryView> getAllOffers();

    OfferDetailsView findById(Long id, String currentUser);

    void deleteOffer(Long id);

    boolean isOwner(String userName, Long id);

    void updateOffer(OfferUpdateServiceModel offerModel);

    OfferAddServiceModel addOffer(OfferAddBindModel offerAddBindModel, String ownerId);
}
