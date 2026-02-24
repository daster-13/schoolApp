package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserInsertDTO(
        @NotNull(message="Το username δεν μπορεί να είναι null.")
        @Size(min =2, max = 20, message ="To username πρεπει να ειναι μεταξύ  2-220 χαρακτήρες.")
        String username,

        @NotNull(message = "Το password δεν μπορέι να είναι null.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=])^.{8,}$",
                message = "To password Πρεπει να περιεχει τουλαχιστον 1 πεζό, 1 κεφαλαίο, 1 ψηφίο και 1 ειδικό χαρακτήρα χωρις κενά")
        String password,

        @NotNull(message = "Ο ρόλος δεν μπορει να είναι κενός")
        Long roleId
) {
    public static UserInsertDTO empty(){
        return new UserInsertDTO("","", 0L);
    }
}
