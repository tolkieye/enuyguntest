Feature: enuygun

  Background: System Login
  Given Open the https://www.enuygun.com URL
  Then I see home page

  @1
  Scenario: Bilet
    Then I verify the area neredenTxt by read only at index 1
    Then I enter "İstanbul" text to neredenInput at index 1
    When I click element: SAW at index 1
    Then I verify the area nereyeTxt by read only at index 1
    Then I enter "İzmir" text to nereyeInput at index 1
    When I click element: ADB at index 1
    Then I verify the area gidisTarihiTxt by read only at index 1
    When I click element: gidisTarihi at index 1
    Then I click element: gidisTarihSec at index 1
    Then I verify the area donusTarihiTxt by read only at index 1
    When I click element: donusTarihi at index 1
    Then I click element: donusTarihSec at index 1
    When I click element: aktarmasiz at index 1
    When I click element: yolcuSecim at index 1
    When I click element: yetiskinArttir at index 1
    When I click element: biletBulBtn at index 1
    Then I see biletSecim page
    Then I wait aramayiDuzenle element 30 seconds at index 1
    Then I wait aramayiFavorilereEkle element 30 seconds at index 1
    Then I need to just wait



