# Description

Optimizations Service system is takes a group of campaigns, associated within a campaign group, and performs an
optimisation on them based on some criteria

# Tech Stack

* Java 17
    * [source](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
    * [documentation](https://docs.oracle.com/en/java/javase/17/)
* [Spring Boot 2.6.3](https://spring.io/blog/2022/01/20/spring-boot-2-6-3-is-now-available)
* [Junit 5.8.2](https://junit.org/junit5/docs/snapshot/release-notes/#release-notes-5.8.2)
* [H2 1.4.200](http://www.h2database.com/html/quickstart.html)
* [Flyway 8.4.3](https://flywaydb.org/documentation/learnmore/releaseNotes)
* [DbRider 1.32.2](https://database-rider.github.io/database-rider/latest/documentation.html?theme=foundation)

# Application Endpoints (Request/Response)

To test endpoints feel free to use any of rest client tools

* View all campaign groups

Request

```
curl -X 'GET' \
  'http://localhost:8888/api/v1/campaigngroups' \
  -H 'accept: application/json'
```

Response Body

```
[
  {
    "id": 1,
    "name": "campaigns"
  }
]
```

* View all campaigns for a campaign group

Request

```curl -X 'GET' \
  'http://localhost:8888/api/v1/campaigngroups/1/campaigns' \
  -H 'accept: application/json'
```

Response Body

```
[
  {
    "id": 1,
    "campaignGroupId": 1,
    "name": "2021-July-BOF-Books",
    "budget": 2108,
    "impressions": 36358,
    "revenue": null
  },
  {
    "id": 2,
    "campaignGroupId": 1,
    "name": "test",
    "budget": 2108,
    "impressions": 36358,
    "revenue": null
  },
  {
    "id": 3,
    "campaignGroupId": 1,
    "name": "3_299_BBQ_G-A_CV_SHP",
    "budget": 674,
    "impressions": 29980,
    "revenue": null
  },
  {
    "id": 4,
    "campaignGroupId": 1,
    "name": "3_299_Bulbs_G-A_CV_SHP",
    "budget": 2000,
    "impressions": 57561,
    "revenue": null
  },
  {
    "id": 5,
    "campaignGroupId": 1,
    "name": "3_299_Containers_G-A_OT_SHP",
    "budget": 500,
    "impressions": 25864,
    "revenue": null
  },
  {
    "id": 6,
    "campaignGroupId": 1,
    "name": "3_299_Furniture_G-A_CV_SHP",
    "budget": 1023,
    "impressions": 68640,
    "revenue": null
  },
  {
    "id": 7,
    "campaignGroupId": 1,
    "name": "3_299_Gifts_AOC_G-A_OT_SHP",
    "budget": 500,
    "impressions": 32743,
    "revenue": null
  },
  {
    "id": 8,
    "campaignGroupId": 1,
    "name": "3_299_Lawn_Care_G-A_CV_SHP",
    "budget": 4600,
    "impressions": 31023,
    "revenue": null
  },
  {
    "id": 9,
    "campaignGroupId": 1,
    "name": "3_299_Vegepod_G-A_CV_SHP",
    "budget": 1325,
    "impressions": 15209,
    "revenue": null
  },
  {
    "id": 10,
    "campaignGroupId": 1,
    "name": "3_299_Wild_Bird_G-A_AOC_SHP",
    "budget": 500,
    "impressions": 4931,
    "revenue": null
  },
  {
    "id": 11,
    "campaignGroupId": 1,
    "name": "Optily-July2021-TOF-Test",
    "budget": 1,
    "impressions": 0,
    "revenue": null
  }
]
```

* View latest optimisations for a campaign group

Request

```
curl -X 'GET' \
  'http://localhost:8888/api/v1/campaigngroups/1/optimisations' \
  -H 'accept: application/json'
```

Response Body

```
{
  "id": 1,
  "campaignGroupId": 1,
  "status": "NOT_APPLIED"
}
```

* View latest recommendations for an optimisation

Request

```
curl -X 'GET' \
  'http://localhost:8888/api/v1/optimisations/1/recommendations' \
  -H 'accept: application/json'
```

Response Body

```
[
  {
    "id": null,
    "campaignId": 1,
    "optimisationId": 1,
    "recommendedBudget": 1646.7366528182552
  },
  {
    "id": null,
    "campaignId": 2,
    "optimisationId": 1,
    "recommendedBudget": 1646.7366528182552
  },
  {
    "id": null,
    "campaignId": 3,
    "optimisationId": 1,
    "recommendedBudget": 1357.8625021038365
  },
  {
    "id": null,
    "campaignId": 4,
    "optimisationId": 1,
    "recommendedBudget": 2607.0688286724126
  },
  {
    "id": null,
    "campaignId": 5,
    "optimisationId": 1,
    "recommendedBudget": 1171.4394848036568
  },
  {
    "id": null,
    "campaignId": 6,
    "optimisationId": 1,
    "recommendedBudget": 3108.861979466556
  },
  {
    "id": null,
    "campaignId": 7,
    "optimisationId": 1,
    "recommendedBudget": 1483.0050669241468
  },
  {
    "id": null,
    "campaignId": 8,
    "optimisationId": 1,
    "recommendedBudget": 1405.1023483244605
  },
  {
    "id": null,
    "campaignId": 9,
    "optimisationId": 1,
    "recommendedBudget": 688.8502599899016
  },
  {
    "id": null,
    "campaignId": 10,
    "optimisationId": 1,
    "recommendedBudget": 223.33622407851962
  },
  {
    "id": null,
    "campaignId": 11,
    "optimisationId": 1,
    "recommendedBudget": 0
  }
]
```

* Apply latest optimisation to campaigns

Request

```
curl -X 'POST' \
  'http://localhost:8888/api/v1/optimisations/1/recommendations' \
  -H 'accept: application/json' \
  -d ''
```

Response Body

```
{
  "message": "Updated Campaigns 11"
}
```

Feel free if you want to use [OpenAPI Client](http://localhost:8888/swagger-ui/index.html#/) for testing endpoints.

# How to run application and tests

- /scripts/run.sh running the application from terminal or from IDEA and open in browser http://localhost:8888
- /scripts/run-tests.sh running all existing and enabled tests

# References

- Many Thanks to [walshdanny](https://github.com/walshdanny700/campaign_optimisation) and his invented bicycle for reuse
- [How do I apply recommendations?](https://help.optily.app/en/articles/4550288-how-do-i-apply-recommendations)
- Many thanks [Throben Janssen](https://thorben-janssen.com/) for following solutions
    - [Result Set Mapping: Constructor Result Mappings](https://thorben-janssen.com/result-set-mapping-constructor-result-mappings/)
    - [Spring Data JPA ??? How to Return DTOs from Native Queries](https://thorben-janssen.com/spring-data-jpa-dto-native-queries/#NamedNativeQuery_with_an_SqlResultSetMapping)
    - [Lombok & Hibernate: How to Avoid Common Pitfalls](https://thorben-janssen.com/lombok-hibernate-how-to-avoid-common-pitfalls/)

# Nice To have

- Test Container integrations
- Add more test cases to cover all business logic
- May have csv data processor (DataProcessorFactory and proper implementations for csv, xml, etc), which may take csv file and seed the database now we have that part extracted in script which using flyway
- Add oneToMany, manyToOne relations and restructure entities
- And many more which will improve this nice application :)
