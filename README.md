# Currency-Converter-2
An update on my original currency converter. Added an API call to gather updated rates and images.

This new converter has a updated UI. Allows selection of currency from a Spinner widget. (Much cleaner)

Also, the previous version had hardcoded values that would require an entire application update each time to update the rates.
This new application called a currency rate API (fixer.io) and takes the JSON information from the call to keep the rates
current and up to date.

Instead of having a toast display your currency amount, this new version displays the information on a table for the user to read.

Finally, an image at the bottom of the screen is updated each time a currency is selected. Allow the user to get a small display
of what currency they are converting to.

EDIT: 
Fixer.io API's free version only pulls rates for euro conversion. So, I had to change the baseline code to be EUR -> X currency converter. 
