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

* List of CampaignGroups:                        GET:  `api/v1/campaign/campaigngroups`
* List of CampaignsForGroup:                     GET:  `api/v1/campaign/campaigngroups/{campaignGroupId}/campaigns`
* Optimisation forForGroup:                      GET:  `api/v1/campaign/campaigngroups/{campaignGroupId}/optimisations`
* List of Recommendations for Optimisation:      GET:  `api/v1/campaign/optimisations/{optimisationId}/recommendations`
* Apply Recommendations                          POST: `api/v1/campaign/optimisations/{optimisationId}/recommendations`

### Open Api Documentation page
* http://localhost:8888/swagger-ui/index.html#/

# Run Application
- /scripts/run.sh run from terminal or from IDEA and open in browser http://localhost:8888

# References
- Thanks [walshdanny](https://github.com/walshdanny700/campaign_optimisation) for invented Bike to 
- [How do I apply recommendations?](https://help.optily.app/en/articles/4550288-how-do-i-apply-recommendations)

# Nice To have
- Batch support (need to add sequence generation for id's :)
- DbRider Integration
- Test Container integrations
- Add more test cases to cover all busines logic
