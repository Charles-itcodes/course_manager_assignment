package se.lexicon.course_manager_assignment.dto.forms;



import se.lexicon.course_manager_assignment.messages.ValidationMessages;
import se.lexicon.course_manager_assignment.validators.UniqueEmail;


import javax.validation.constraints.*;

public class CreateStudentForm {

    @Null
    private Integer id;

    @NotBlank(message = ValidationMessages.IS_REQUIRED)
    @Pattern(regexp = "^([a-zA-Z]+[\\'\\,\\.\\-]?[a-zA-Z ]*)+[ ]([a-zA-Z]+[\\'\\,\\.\\-]?[a-zA-Z ]+)+$", message = "Not a valid Full name")
    private String name;

    @NotBlank(message = ValidationMessages.IS_REQUIRED)
    @Email(regexp = "^(\\D)+(\\w)*((\\.(\\w)+)?)+@(\\D)+(\\w)*((\\.(\\D)+(\\w)*)+)?(\\.)[a-z]{2,}$", flags = Pattern.Flag.CASE_INSENSITIVE, message = ValidationMessages.NOT_A_VALID_EMAIL_ADDRESS)
    @UniqueEmail
    private String email;

    @NotBlank(message = ValidationMessages.IS_REQUIRED)
    @Size(max = 255, message = "Address can't have more than 255 letters")
    private String address;

    public CreateStudentForm(Integer id, String name, String email, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public CreateStudentForm() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
