package au.org.ala.biocollect.merit
import grails.converters.JSON

class SearchController {
    def searchService, webService, speciesService, grailsApplication, commonService, projectActivityService

    /**
     * Main search page that takes its input from the search bar in the header
     * @param query
     * @return resp
     */
    def index(String query) {
        params.facets = SettingService.getHubConfig().availableFacets.join(',')+',className'
        [facetsList: params.facets.tokenize(","), results: searchService.fulltextSearch(params)]
    }

    /**
     * Handles queries to support autocomplete for species fields.
     * @param q the typed query.
     * @param limit the maximum number of results to return
     * @return
     */
    def species(String q, Integer limit) {
        render speciesService.searchForSpecies(q, limit, params.listId) as JSON
    }

    def searchSpeciesList(String sort, Integer max, Integer offset){
        render speciesService.searchSpeciesList(sort, max, offset) as JSON
    }

    //Search species by project activity species constraint.
    def searchSpecies(String id, String q, Integer limit){
        def pActivity = projectActivityService.get(id)
        def result
        switch(pActivity?.species?.type){
            case 'SINGLE_SPECIES':
                result = speciesService.searchForSpecies(pActivity?.species?.singleSpecies?.name, 1)
                break

            case 'ALL_SPECIES':
                result = speciesService.searchForSpecies(q, limit)
                break

            case 'GROUP_OF_SPECIES':
                def lists = pActivity?.species?.speciesLists
                result = speciesService.searchSpeciesInLists(q, lists, limit)
                break
            default:
                result = [autoCompleteList: []]
                break
        }
        render result as JSON
    }

    @PreAuthorise(accessLevel = 'siteReadOnly', redirectController ='home', redirectAction = 'index')
    def downloadSearchResults() {
        def path = 'search/downloadSearchResults'
        if (params.view == 'xlsx') {
             path += ".xlsx"
        }
        def facets = []
        facets.addAll(params.getList("fq"))
        facets << "className:au.org.ala.ecodata.Project"
        params.put("fq", facets)
        def url = grailsApplication.config.ecodata.service.url + path +  commonService.buildUrlParamsFromMap(params)
        webService.proxyGetRequest(response, url, true, true)
    }

    @PreAuthorise(accessLevel = 'siteAdmin', redirectController ='home', redirectAction = 'index')
    def downloadAllData() {

        params.query = "docType:project"
        def path = "search/downloadAllData"

        if (params.view == 'xlsx' || params.view == 'json') {
            path += ".${params.view}"
        }else{
            path += ".json"
        }

        def facets = []
        facets.addAll(params.getList("fq"))
        facets << "className:au.org.ala.ecodata.Project"
        params.put("fq", facets)

        def url = grailsApplication.config.ecodata.service.url + path +  commonService.buildUrlParamsFromMap(params)
        webService.proxyGetRequest(response, url, true, true,960000)
    }

    @PreAuthorise(accessLevel = 'siteAdmin', redirectController ='home', redirectAction = 'index')
    def downloadSummaryData() {
        params.query = "docType:project"
        def path = "search/downloadSummaryData"

        if (params.view == 'xlsx' || params.view == 'json') {
            path += ".${params.view}"
        }else{
            path += ".json"
        }

        def url = grailsApplication.config.ecodata.service.url + path + commonService.buildUrlParamsFromMap(params)
        webService.proxyGetRequest(response, url, true, true,960000)
    }

}
