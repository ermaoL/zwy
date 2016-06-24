/**
 * Created by Administrator on 2016/6/24.
 */
var myAdminApp = angular.module('myAdminApp', [
    'ui.router',
    'tm.pagination']);

myAdminApp.config(['$stateProvider', '$locationProvider', '$urlRouterProvider',
    function ($stateProvider, $locationProvider, $urlRouterProvider) {
        'use strict';

        // Set the following to true to enable the HTML5 Mode
        // You may have to set <base> tag in index and a routing configuration in your server
        $locationProvider.html5Mode(false);

        // defaults to import.list
        $urlRouterProvider.otherwise('loginAdmin');

        //
        // Application Routes
        // -----------------------------------
        $stateProvider
            .state('loginAdmin', {
                url: '/admin/login',
                templateUrl: 'login-admin.html'
            })
            .state('mainAdmin', {
                url: '/admin/main',
                templateUrl: 'main-admin.html'
            })
            .state('mainAdmin.importList', {
                url: '/import/list',
                templateUrl: 'views/admin/import-list-admin.html'
            })
            .state('mainAdmin.importDetail', {
                url: '/import/detail',
                templateUrl: 'views/admin/import-detail-admin.html'
            })
            .state('mainAdmin.exportList', {
                url: '/export/list',
                templateUrl: 'views/admin/export-list-admin.html'
            })
            .state('mainAdmin.exportDetail', {
                url: '/export/detail',
                templateUrl: 'views/admin/export-detail-admin.html'
            })
            .state('mainAdmin.ticketFollow', {
                url: '/ticket/follow',
                templateUrl: 'views/admin/ticket-follow-admin.html'
            })
            .state('mainAdmin.singleTicketFollow', {
                url: '/single/ticket/follow',
                templateUrl: 'views/admin/single-ticket-follow-admin.html'
            })
            .state('mainAdmin.transitOrder', {
                url: '/transit/order',
                templateUrl: 'views/admin/transit-order-admin.html'
            })
            .state('main.billInquiry', {
                url: '/bill/inquiry',
                templateUrl: 'views/admin/bill-inquiry-admin.html'
            })
            .state('main.userManage', {
                url: '/user/manage',
                templateUrl: 'views/admin/user-manage-admin.html'
            })
    }]);