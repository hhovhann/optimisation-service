# optimisation-service
Optimizations Service Project

# Tech Stack
* Java 17
* Spring Boot 2.6.3
* Junit 5.8.2
* H2 1.4.200
* Flyway 8.4.3

# Application Endpoints
To test endpoints feel free to use any of rest client tools

* View all campaign groups:                                    GET:  `api/v1/campaign/campaigngroups`
* View all campaigns for a campaign group:                     GET:  `api/v1/campaign/campaigngroups/{campaignGroupId}/campaigns`
* View latest optimisations for a campaign group:              GET:  `api/v1/campaign/campaigngroups/{campaignGroupId}/optimisations`
* View latest recommendations for an optimisation:             GET:  `api/v1/campaign/optimisations/{optimisationId}/recommendations`
* Apply latest optimisation to campaigns                       POST: `api/v1/campaign/optimisations/{optimisationId}/recommendations`

Please use open API client for reaching endpoints. Information defined in section bellow

### Open Api Documentation page
* http://localhost:8888/swagger-ui/index.html#/

# Run Application
- /scripts/run.sh run from terminal or from IDEA and open in browser http://localhost:8888

# References
- Many Thanks to [walshdanny](https://github.com/walshdanny700/campaign_optimisation) and his invented bicycle for reuse
- [How do I apply recommendations?](https://help.optily.app/en/articles/4550288-how-do-i-apply-recommendations)

# Nice To have
- Batch support (need to add sequence generation for entity id's which would use batch :)
- Cashing for all campaigns could be added
- DBRider Integration
- Test Container integrations
- OpenApi documentation specification improvement for all methods
- Add more test cases to cover all business logic
