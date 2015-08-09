angular
    .module('app')
    .factory('ApiService', [
        '$http', '$window',  'Session', '$translate', '$location', function($http, $window, Session, $translate, $location) {
            var service = {};
            service.apiUrl = function (){
                var url = '';
                switch($location.host()){
                    case "test.localhost":
                        url = "http://test.localhost:9090/";
                        break;
                    case "www.neerpoints.com":
                        url = "http://services.neerpoints.com/";
                        break;
                    case "test.neerpoints.com":
                        url = "http://test.services.neerpoints.com/";
                        break;
                    default :
                        url = "http://localhost:9090/";
                }
                return url;
            };

            service.callApi = function(method, path, data) {
                var userId = Session.user ? Session.user.companyUserId : '';
                var apiKey = Session.user ? Session.user.apiKey : '';
                console.log(Session.user);
                return $http({
                    method: method,
                    url: service.apiUrl() + "api/" + path,
                    data: data,
                    headers: {'Content-Type': 'application/json', 'Api-Key': apiKey, 'User-Id': userId, 'Language': $translate.use()}
                })
            };

            service.callAuthApi = function(method, path, data) {
                var key = Session.user ? Session.user.apiKey : '';
                return $http({
                    method: method,
                    url: service.apiUrl() + "auth/" + path,
                    data: data,
                    headers: {'Content-Type': 'application/json', 'Language': $translate.use()}
                })
            };
            return service;
        }
    ]);