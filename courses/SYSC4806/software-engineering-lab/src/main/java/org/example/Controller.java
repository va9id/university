package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
@RequestMapping("/addressbook")
public class Controller {

    private final AddressBookRepository addressBookRepository;
    private final BuddyInfoRepository buddyInfoRepository;

    @Autowired
    public Controller(AddressBookRepository addressBookRepository, BuddyInfoRepository buddyInfoRepository) {
        this.addressBookRepository = addressBookRepository;
        this.buddyInfoRepository = buddyInfoRepository;
    }

    @GetMapping("/create")
    public String getCreateAddressBook() {
        return "createAddressBook";
    }
    @PostMapping("/create")
    public String showCreateAddressBook(Model model) {
        AddressBook addressBook = new AddressBook();
        addressBookRepository.save(addressBook);
        model.addAttribute("addressBook", addressBook);
        return "createAddressBook";
    }

    @GetMapping("/{addressBookId}/addBuddy")
    public String getAddBuddy(@PathVariable Long addressBookId, Model model) {
        AddressBook addressBook = addressBookRepository.findById(addressBookId).orElse(null);
        if (addressBook != null) {
            model.addAttribute("addressBook", addressBook);
            model.addAttribute("buddyInfo", new BuddyInfo());
        }
        return "addBuddy";
    }
    @PostMapping("/{addressBookId}/addBuddy")
    public String processAddBuddy(@PathVariable Long addressBookId, @ModelAttribute("buddyInfo") BuddyInfo buddyInfo) {
        AddressBook addressBook = addressBookRepository.findById(addressBookId).orElse(null);
        if (addressBook != null) {
            buddyInfoRepository.save(buddyInfo);
            addressBook.addBuddy(buddyInfo);
            addressBookRepository.save(addressBook);
        }
        return "redirect:/addressbook/list";
    }

    @GetMapping("/list")
    public ModelAndView listBuddies(Model model) {
        Iterable<AddressBook> addressBooks = addressBookRepository.findAll();
        model.addAttribute("addressBooks", addressBooks);
        return new ModelAndView("listBuddies", "model", model);
    }

    @GetMapping("/single")
    public String showSPA(Model model) {
        AddressBook addressBook = new AddressBook();
        addressBookRepository.save(addressBook);
        model.addAttribute("id", addressBook.getId());
        return "index";
    }

    @PostMapping("/{addressBookId}/spaAddBuddy")
    @ResponseBody
    public ResponseEntity<String> processSPA(
            @PathVariable Long addressBookId,
            @RequestBody BuddyInfo buddyInfo ) {
        AddressBook addressBook = addressBookRepository.findById(addressBookId).orElse(null);
        if (addressBook != null) {
            buddyInfoRepository.save(buddyInfo);
            addressBook.addBuddy(buddyInfo);
            addressBookRepository.save(addressBook);
            return new ResponseEntity<>(buddyInfo.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(
                String.format("Error: No AddressBook found with ID=%d", addressBookId),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
