#include "string"
#include "iostream"
#include "fstream"

// TODO: Läs och kolla om det finns ett smartare sätt
// Generell plan: Ska rabbla alla namn, 1 för närvaro, 2 för oanmäld frånvaro och 3 för anmäld frånvaro
// Ska hålla all data i en .csv fil, kanske 1 kolonn/datum? Är en del värden, ska fylla i automagiskt (kanske läsa in
// datum och skriva en ny kolonn??)

int main()
{
    std::fstream fileIn("Närvaro.csv");
    
    if (!fileIn.is_open()){
        std::cout << "Filen finns ej";
        exit(EXIT_FAILURE);
    }
    
    return 0;
}