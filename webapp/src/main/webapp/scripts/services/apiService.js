angular
    .module('app')
    .factory('ApiService', [
        '$http', '$window',  'Session', '$translate', '$location', function($http, $window, Session, $translate, $location) {
            var service = {};
            service.apiUrl = function (){
                var url = '';
                switch($location.host()){
                    case "test.localhost":
                        url = 'http://test.localhost:9090/';
                        break;
                    case "www.monederobingo.com":
                        url = 'http://prod.api.monederobingo.com/';
                        break;
                    case "test.monederobingo.com":
                        url = 'http://uat.api.monederobingo.com/';
                        break;
                    default :
                        url = 'http://' + $location.host() + ':9090/';
                }
                return url;
            };

            service.apiUrlRoot = function(){
                return "api/v1/";
            };

            service.sendRequestToApi = function(method, path, data) {
                var userId = Session.user ? Session.user.companyUserId : '';
                var apiKey = Session.user ? Session.user.apiKey : '';
                return $http({
                    method: method,
                    url: service.apiUrl() +  service.apiUrlRoot() +   path,
                    data: data,
                    headers: {'Content-Type': 'application/json', 'Api-Key': apiKey, 'User-Id': userId}
                })
            };

            service.sendRequest = function(method, path, data) {
                return $http({
                    method: method,
                    url: service.apiUrl() + path,
                    data: data,
                    headers: {'Content-Type': 'application/json'}
                })
            };
            return service;
        }
    ]);