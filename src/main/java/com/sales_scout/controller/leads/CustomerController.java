package com.sales_scout.controller.leads;

import com.sales_scout.dto.error.ResponseError;
import com.sales_scout.dto.EntityFilters.CustomerFilerFields;
import com.sales_scout.dto.request.ProspectRequestDto;
import com.sales_scout.dto.request.update.BulkCustomerEditRequestDto;
import com.sales_scout.dto.response.CustomerResponseDto;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.enums.ProspectStatus;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.service.leads.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prospects")
public class CustomerController {
     @Autowired
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    public CustomerController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    /**
     *  get all prospect
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerResponseDto>> getAllProspect(
            @RequestParam(required = true) Long companyId,
            @RequestParam(required = false) ProspectStatus status
    ){
        try{
            List<CustomerResponseDto> prospects = this.customerService.getAllProspects(companyId, status);
            return new ResponseEntity<>(prospects, HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Retrieves a list of prospects that match the specified filter criteria.
     *
     * @param customerFilerFields an object containing the fields and values to filter the prospects by
     * @return a ResponseEntity containing a list of CustomerResponseDto objects matching the filter criteria.
     *         Returns an HTTP 200 status on success, or throws an exception in case of errors.
     */
    @PostMapping("filter-by-fields")
    public ResponseEntity<List<CustomerResponseDto>> getAllProspectByFilerFields(@RequestBody CustomerFilerFields customerFilerFields ){
        try{
            List<CustomerResponseDto> prospects = this.customerService.getAllProspectsByFilerField(customerFilerFields);
            return new ResponseEntity<>(prospects, HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * this end point id responsible for get customers by search key and company ID
     * @param keyword this filter field
     * @param companyId the company id
     * @return  ResponseEntity<List<CustomerResponseDto>> List of customers
     */
    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseDto>> searchCustomers(@RequestParam(name = "q", required = false) String keyword,
                                                          @RequestParam(name = "companyId", required = true) Long companyId) throws
            DataNotFoundException {
            try {
                List<CustomerResponseDto> customers = customerService.searchCustomers(keyword, companyId);
                return ResponseEntity.ok(customers);
            } catch (DataNotFoundException e){
               throw new DataNotFoundException("custumres not found", 403L);
            }
    }

    /**
     * Retrieves a prospect by its unique ID.
     *
     * @param id the ID of the prospect to be retrieved
     * @return a ResponseEntity containing an Optional of CustomerResponseDto if the prospect is found,
     *         or an empty Optional if the prospect does not exist or has been deleted
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<CustomerResponseDto>> getProspectById(@PathVariable Long id){
        Optional<CustomerResponseDto> prospect = this.customerService.getProspectById(id);
        return new ResponseEntity<>(prospect, HttpStatus.OK);
    }

    @PutMapping("/bulk-edit")
    public ResponseEntity<Boolean> bulkEditCustomers(@RequestBody BulkCustomerEditRequestDto request) throws TransactionSystemException {
        return ResponseEntity.ok(  customerService.bulkEditCustomers(request));
    }


    /**
     * create prospect
     * @param prospectRequestDto
     * @return
     */
    @PostMapping("")
    public ResponseEntity<Customer> createProspect(@RequestBody ProspectRequestDto prospectRequestDto) {
      try {
          // Call the service method to create the Prospect
          Customer prospect = customerService.saveOrUpdateProspect(prospectRequestDto);
          // Return a ResponseEntity with the created Prospect and a 201 CREATED status
          return new ResponseEntity<>(prospect, HttpStatus.CREATED);
      } catch (Exception e) {
          throw new DataNotFoundException(e.getMessage(),404L);
      }
    }

    /**
     * Handles the upload of an Excel file to import prospects associated with a specific company.
     *
     * @param excelFile the MultipartFile representing the uploaded Excel file containing prospect details
     * @param companyId the ID of the company to which the uploaded prospects belong
     * @return a ResponseEntity containing the result of the operation:
     *         - HTTP 201 (Created) with a list of imported prospects upon success
     *         - HTTP 400 (Bad Request) with an error message if the file is missing or empty
     *         - HTTP 409 (Conflict) with an error response if there are duplicate entries
     *         - HTTP 500 (Internal Server Error) with an error response for transaction, file processing, or unexpected errors
     * @throws IOException if there is an error processing the uploaded file
     * @throws DataAlreadyExistsException if duplicate prospect entries are detected
     * @throws TransactionSystemException if there are database transaction errors
     * @throws DataNotFoundException if associated resources are not found
     * @throws Exception for any other unexpected errors
     */
    @PostMapping(value = "import-from-excel", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadProspectFile(@RequestParam("file") MultipartFile excelFile, @RequestParam("companyId") Long companyId)
    throws IOException, DataAlreadyExistsException, TransactionSystemException, Exception, DataNotFoundException{
        if (excelFile == null || excelFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Erreur : Le fichier est manquant ou vide.");
        }
        try {
            List<Customer> prospects = customerService.uploadProspectsFromFile(excelFile, companyId);
            return ResponseEntity.status(HttpStatus.CREATED).body(prospects);

        } catch (DataAlreadyExistsException e) {
            // Handle duplicate entry error
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseError.builder()
                            .message("Erreur : Un prospect avec les mêmes informations existe déjà.")
                            .code(409)
                            .build());

        } catch (TransactionSystemException e) {
            // Handle transaction-related errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseError.builder()
                            .message("Erreur de transaction en base de données. Veuillez vérifier vos données et réessayer.")
                            .code(500)
                            .build());

        } catch (IOException e) {
            // Handle file processing errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseError.builder()
                    .message("Erreur lors du traitement du fichier. Veuillez vérifier le format du fichier.")
                    .code(500)
                    .build());


        } catch (Exception e) {
            // Handle any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseError.builder()
                    .message("Une erreur inattendue s'est produite lors de l'importation. Détails : \" + e.getMessage()")
                    .code(500)
                    .build());
        }
    }

    /**
     * This end point allows to export Excel file contain customers details by the giving company id and
     * List of customers , Else download all customers
     * @param companyId the company id
     * @param customerIds list of customers id
     * @return ResponseEntity<byte[]>
     */
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportProspectsToExcel(
            @RequestParam Long companyId,
            @RequestBody(required = false) List<Long> customerIds) {

        try {
            List<Customer> prospects;
            if (customerIds != null && !customerIds.isEmpty()) {
                prospects = customerRepository.findAllById(customerIds);
            } else {
                prospects = customerRepository.findAllByDeletedAtIsNullAndCompanyId(companyId);
            }

            byte[] excelData = customerService.exportFileExcel(prospects);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("Prospects.xlsx").build());

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Updates the status of a prospect with the provided ID.
     *
     * @param statusId the new status to be set for the prospect
     * @param prospectId the ID of the prospect to be updated
     * @return a {@link ResponseEntity} containing the updated {@link CustomerResponseDto} if the operation is successful,
     *         or an appropriate HTTP status code with a null body in case of errors
     */
    @PutMapping("status")
    public ResponseEntity<CustomerResponseDto> updateProspectStatus(@RequestParam Long statusId, @RequestParam Long prospectId) {
        try {
            // Update the status using a service method
            return ResponseEntity.ok(customerService.updateProspectStatus(statusId, prospectId));
        } catch (EntityNotFoundException e) {
            // Handle the exception if the prospect is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Catch other exceptions
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Soft Delete Prospect By ID
     * @param id
     * @return
     */
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Boolean>  deleteById(@PathVariable Long id)throws EntityNotFoundException{
        try{
            return ResponseEntity.ok().body(customerService.softDeleteById(id));
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * Restore Prospect By ID
     * @param id the ID of the customer
     * @return ResponseEntity<Boolean>
     */
    @PutMapping("/restore/{id}")
    public ResponseEntity<Boolean> restoreProspectById(@PathVariable Long id)throws EntityNotFoundException {
        try{
            return ResponseEntity.ok().body(customerService.restoreProspectById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * Bulk Soft delete customers
     * @param ids ids of customers to soft-delete
     * @return  ResponseEntity<Boolean>
     * @throws EntityNotFoundException if data not found
     */
    @PostMapping("/soft-delete/bulk")
    public ResponseEntity<Boolean> bulkSoftDelete(@RequestBody List<Long> ids)throws EntityNotFoundException{
        try{
            return ResponseEntity.ok().body(customerService.bulkSoftDelete(ids));
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }


    @GetMapping("/export-excel-template")
    public ResponseEntity<byte[]> exportExcelFile(@RequestParam Long companyId) {
        try {
            byte[] excelData = customerService.generateProspectUploadExcel(companyId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("Updated_Prospects.xlsx").build());

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
