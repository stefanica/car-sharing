package carsharing;

import java.util.List;

public interface CompanyDAO {
    void createCompany(Company company);
    Company getCompanyById(int id);
    List<Company> getAllCompanies();
    void updateCompany(Company company);
    void deleteCompany(int id);
}
