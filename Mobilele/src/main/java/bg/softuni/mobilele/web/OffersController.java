package bg.softuni.mobilele.web;

import bg.softuni.mobilele.model.binding.OfferAddBindModel;
import bg.softuni.mobilele.model.binding.OfferUpdateBindingModel;
import bg.softuni.mobilele.model.entity.enums.EngineEnum;
import bg.softuni.mobilele.model.entity.enums.TransmissionEnum;
import bg.softuni.mobilele.model.service.OfferAddServiceModel;
import bg.softuni.mobilele.model.service.OfferUpdateServiceModel;
import bg.softuni.mobilele.model.view.OfferDetailsView;
import bg.softuni.mobilele.service.BrandService;
import bg.softuni.mobilele.service.OfferService;
import bg.softuni.mobilele.service.impl.MobileleUser;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class OffersController {
    private final OfferService offerService;
    private final ModelMapper modelMapper;
    private final BrandService brandService;

    public OffersController(OfferService offerService, ModelMapper modelMapper, BrandService brandService) {
        this.offerService = offerService;
        this.modelMapper = modelMapper;
        this.brandService = brandService;
    }

    // GET
    @GetMapping("/offers/all")
    public String allOffers(Model model) {

        model.addAttribute("offers", offerService.getAllOffers());

        return "offers";
    }

    @GetMapping("/offers/{id}/details")
    public String showOffer(@PathVariable Long id, Model model, Principal principal) {

        model.addAttribute("offer", this.offerService.findById(id, principal.getName()));

        return "details";
    }

    @PreAuthorize("isOwner(#id)")
    @DeleteMapping("/offers/{id}")
    public String deleteOffer(@PathVariable Long id) {

        offerService.deleteOffer(id);

        return "redirect:/offers/all";
    }

    @GetMapping("/offers/{id}/edit")
    public String editOffer(@PathVariable Long id,
                            Model model,
                            @AuthenticationPrincipal MobileleUser currentUser) {

        OfferDetailsView offerDetailsView = offerService.findById(id, currentUser.getUserIdentifier());

        OfferUpdateBindingModel offerModel = modelMapper.map(offerDetailsView, OfferUpdateBindingModel.class);

        model.addAttribute("engines", EngineEnum.values());
        model.addAttribute("transmissions", TransmissionEnum.values());
        model.addAttribute("offerModel", offerModel);

        return "update";
    }

    @GetMapping("/offers/{id}/edit/errors")
    public String editOfferErrors(@PathVariable Long id,
                                  Model model) {

        model.addAttribute("engines", EngineEnum.values());
        model.addAttribute("transmissions", TransmissionEnum.values());

        return "update";
    }

    @PatchMapping("/offers/{id}/edit")
    public String editOffer(@PathVariable Long id,
                            @Valid OfferUpdateBindingModel offerModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("offerModel", offerModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.offerModel", bindingResult);

            return "redirect:/offers/" + id + "/edit/errors";
        }

        OfferUpdateServiceModel serviceModel = modelMapper.map(offerModel, OfferUpdateServiceModel.class);

        serviceModel.setId(id);
        offerService.updateOffer(serviceModel);

        return "redirect:/offers/" + id + "/details";
    }

    @GetMapping("/offers/add")
    public String getAddOfferPage(Model model) {

        if (!model.containsAttribute("offerAddBindModel")) {
            model
                    .addAttribute("offerAddBindModel", new OfferAddBindModel())
                    .addAttribute("brandsModels", brandService.getAllBrands());
        }

        return "offer-add";
    }

    @PostMapping("/offers/add")
    public String addOffer(@Valid OfferAddBindModel offerAddBindModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal MobileleUser user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("offerAddBindModel", offerAddBindModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.offerAddBindModel", bindingResult)
                    .addFlashAttribute("brandsModels", brandService.getAllBrands());
            return "redirect:/offers/add";
        }
        OfferAddServiceModel savedOfferAddServiceModel = offerService.addOffer(offerAddBindModel, user.getUserIdentifier());
        return "redirect:/offers/" + savedOfferAddServiceModel.getId() + "/details";
    }
}
