package bg.softuni.mobilele.service.impl;

import bg.softuni.mobilele.model.binding.OfferAddBindModel;
import bg.softuni.mobilele.model.entity.ModelEntity;
import bg.softuni.mobilele.model.entity.OfferEntity;
import bg.softuni.mobilele.model.entity.UserEntity;
import bg.softuni.mobilele.model.entity.UserRoleEntity;
import bg.softuni.mobilele.model.entity.enums.EngineEnum;
import bg.softuni.mobilele.model.entity.enums.TransmissionEnum;
import bg.softuni.mobilele.model.entity.enums.UserRoleEnum;
import bg.softuni.mobilele.model.service.OfferAddServiceModel;
import bg.softuni.mobilele.model.service.OfferUpdateServiceModel;
import bg.softuni.mobilele.model.view.OfferDetailsView;
import bg.softuni.mobilele.model.view.OfferSummaryView;
import bg.softuni.mobilele.repository.ModelRepository;
import bg.softuni.mobilele.repository.OfferRepository;
import bg.softuni.mobilele.repository.UserRepository;
import bg.softuni.mobilele.service.OfferService;
import bg.softuni.mobilele.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ModelRepository modelRepository;
    private final UserRepository userRepository;

    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper,
                            ModelRepository modelRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.modelRepository = modelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void initializeOffers() {

        if (offerRepository.count() == 0) {
            OfferEntity offer1 = new OfferEntity();
            offer1
                    .setModel(modelRepository.findById(1L).orElse(null))
                    .setEngine(EngineEnum.GASOLINE)
                    .setTransmission(TransmissionEnum.MANUAL)
                    .setMileage(22500)
                    .setPrice(14300)
                    .setYear(2019)
                    .setDescription("Used, but well services and in good condition.")
                    .setSeller(userRepository.findByUsername("pesho")
                            .orElse(null)) // or currentUser.getUserName()
                    .setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcXp1KBpDKgYs6VqndkBpX8twjPOZbHV86yg&usqp=CAU");

            OfferEntity offer2 = new OfferEntity();
            offer2
                    .setModel(modelRepository.findById(1L).orElse(null))
                    .setEngine(EngineEnum.DIESEL)
                    .setTransmission(TransmissionEnum.AUTOMATIC)
                    .setMileage(209000)
                    .setPrice(5500)
                    .setYear(2000)
                    .setDescription("After full maintenance, insurance, new tires...")
                    .setSeller(userRepository.findByUsername("admin")
                            .orElse(null))
                    .setImageUrl("https://www.picclickimg.com/d/l400/pict/283362908243_/FORD-ESCORT-MK5-16L-DOHC-16v-ZETEC.jpg");

            offerRepository.saveAll(List.of(offer1, offer2));
        }
    }

    @Override
    public List<OfferSummaryView> getAllOffers() {
        return this.offerRepository
                .findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public OfferDetailsView findById(Long id, String currentUser) {
        return this.offerRepository
                .findById(id)
                .map(o -> mapDetailsView(currentUser, o))
                .get();
    }

    @Override
    public void deleteOffer(Long id) {
        this.offerRepository.deleteById(id);
    }

    public boolean isOwner(String userName, Long id) {
        Optional<OfferEntity> offer = this.offerRepository.findById(id);
        Optional<UserEntity> user = this.userRepository.findByUsername(userName);

        if (offer.isEmpty() || user.isEmpty()) {
            return false;
        }

        OfferEntity offerEntity = offer.get();

        return isAdmin(user.get()) || offerEntity.getSeller().getUsername().equals(userName);
    }

    private boolean isAdmin(UserEntity user) {
        return user
                .getRoles()
                .stream()
                .map(UserRoleEntity::getRole)
                .anyMatch(r -> r == UserRoleEnum.ADMIN);
    }


    @Override
    public void updateOffer(OfferUpdateServiceModel offerModel) {

        OfferEntity offerEntity = this.offerRepository
                .findById(offerModel.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Offer with id " + offerModel.getId() + " not found!"));

        offerEntity.setPrice(offerModel.getPrice())
                .setDescription(offerModel.getDescription())
                .setEngine(offerModel.getEngine())
                .setImageUrl(offerModel.getImageUrl())
                .setMileage(offerModel.getMileage())
                .setTransmission(offerModel.getTransmission())
                .setYear(offerModel.getYear());

        offerRepository.save(offerEntity);
    }

    @Override
    public OfferAddServiceModel addOffer(OfferAddBindModel offerAddBindModel, String ownerId) {
        UserEntity userEntity = userRepository.findByUsername(ownerId).orElseThrow();

        OfferAddServiceModel offerAddServiceModel = modelMapper.map(offerAddBindModel, OfferAddServiceModel.class);

        OfferEntity newOffer = modelMapper.map(offerAddServiceModel, OfferEntity.class);

        newOffer.setCreated(Instant.now());
        newOffer.setSeller(userEntity);

        ModelEntity model = modelRepository.getById(offerAddBindModel.getModelId());

        newOffer.setModel(model);

        OfferEntity savedOffer = offerRepository.save(newOffer);

        return modelMapper.map(savedOffer, OfferAddServiceModel.class);
    }

    private OfferSummaryView map(OfferEntity offerEntity) {
        OfferSummaryView summaryView = this.modelMapper.map(offerEntity, OfferSummaryView.class);

        summaryView.setModel(offerEntity.getModel().getName());
        summaryView.setBrand(offerEntity.getModel().getBrand().getName());

        return summaryView;
    }

    private OfferDetailsView mapDetailsView(String currentUser, OfferEntity offer) {
        OfferDetailsView offerDetailsView = this.modelMapper.map(offer, OfferDetailsView.class);

        offerDetailsView.setCanDelete(isOwner(currentUser, offer.getId()));
        offerDetailsView.setModel(offer.getModel().getName());
        offerDetailsView.setBrand(offer.getModel().getBrand().getName());
        offerDetailsView.setSellerFullName(offer.getSeller().getFirstName() + " " + offer.getSeller().getLastName());

        return offerDetailsView;
    }
}
