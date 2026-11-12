package bg.softuni.mobilele.service;


import bg.softuni.mobilele.model.view.BrandViewModel;

import java.util.List;

public interface BrandService {

    void initializeBrand();

    List<BrandViewModel> getAllBrands();
}
