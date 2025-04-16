## 📁 Project Structure

```
src/main/java/ghkg/
├── ApiApp.java                            
├── config                                
│   └── OpenApiConfig.java                 
├── controller                             
│   ├── v1                                 
│   │   ├── GarageController.java          
│   │   ├── TripController.java            
│   │   └── ...                            
│   └── v2                                 
│       └── 
├── model                                  
│   ├── Car.java
│   └── Trip.java
├── service                                
│   ├── GarageService.java
│   └── TripService.java
└── repository
    ├── GarageRepository.java
    └── TripRepository.java                           
```

> 💡 Wersja `/v1` działająca.  
> 🧱 Wersja `/v2` zbugowana.