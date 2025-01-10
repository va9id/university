$(document).ready(function () {
    // Function to handle checkbox change
    function handleCheckboxChange() {
        const atLeastOneChecked = $(".checkbox-group:checked").length > 0;

        // Check if at least one checkbox is checked
        if (atLeastOneChecked) {
            // Remove "required" attribute from the form
            $(".checkbox-group").removeAttr("required");
        } else {
            // Add "required" attribute to the form
            $(".checkbox-group").attr("required", "required");
        }
    }

    // Attach the handleCheckboxChange function to the change event of checkboxes
    $(".checkbox-group").change(handleCheckboxChange);

    // Trigger the initial checkbox check to set the required attribute accordingly
    handleCheckboxChange();
})