angular
    .module('app')
    .controller('settingsUsersCtrl', [
        '$scope', '$http', 'ApiService', 'Session', '$window', '$translate',
        function($scope, $http, ApiService, Session, $window, $translate) {
            if (Session.isClosed()) {
                $window.location.href = "/#/";
            }
            $scope.showMessage = false;
            $scope.users = {};
            getUsers();

            function getUsers() {
                $scope.isProcessing = true;
                ApiService.sendRequestToApi('GET', 'company_users/' + Session.user.companyId)
                    .success(function(data) {
                        $scope.isProcessing = false;
                        $scope.users = data.object;
                    })
                    .error(function() {
                        $scope.isProcessing = false;
                        $scope.isError = true;
                        $scope.message = $translate.instant('AN_ERROR_OCCURRED');
                        $scope.showMessage = true;
                    });
            }
        }
    ]);