'use strict';

angular.module('aquilaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('claimCase', {
                parent: 'entity',
                url: '/claimCase',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aquilaApp.claimCase.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/claimCase/claimCases.html',
                        controller: 'ClaimCaseController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('claimCase');
                        return $translate.refresh();
                    }]
                }
            })
            .state('claimCaseDetail', {
                parent: 'entity',
                url: '/claimCase/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aquilaApp.claimCase.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/claimCase/claimCase-detail.html',
                        controller: 'ClaimCaseDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('claimCase');
                        return $translate.refresh();
                    }]
                }
            });
    });
