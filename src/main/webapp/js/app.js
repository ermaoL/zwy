/**
 * Created by Administrator on 2016/6/24.
 */
var myApp = angular.module('myApp', [
    'ui.router',
    'tm.pagination']);

myApp.config(['$stateProvider', '$locationProvider', '$urlRouterProvider', '$httpProvider',
    function ($stateProvider, $locationProvider, $urlRouterProvider, $httpProvider) {
        'use strict';

        // Set the following to true to enable the HTML5 Mode
        // You may have to set <base> tag in index and a routing configuration in your server
        $locationProvider.html5Mode(false);
        // $httpProvider.defaults.headers.common['Cache-Control'] = 'no-cache';
        if (!$httpProvider.defaults.headers.get) {
            $httpProvider.defaults.headers.get = {};
        }

        $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
        $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

        // defaults to login
        $urlRouterProvider.otherwise('login');

        //
        // Application Routes
        // -----------------------------------
        $stateProvider.state('login', {
            url: '/login',
            templateUrl: 'login.html'
        }).state('register', {
            url: '/register',
            templateUrl: 'register.html'
        }).state('index', {
            url: '/main',
            templateUrl: 'main.html'
        }).state('index.importList', {
            url: '/import/list',
            templateUrl: 'views/import-list.html'
        }).state('index.importDetail', {
            url: '/import/detail',
            templateUrl: 'views/import-detail.html'
        }).state('index.exportList', {
            url: '/export/list',
            templateUrl: 'views/export-list.html'
        }).state('index.exportDetail', {
            url: '/export/detail',
            templateUrl: 'views/export-detail.html'
        }).state('index.ticketFollow', {
            url: '/ticket/follow',
            templateUrl: 'views/ticket-follow.html'
        }).state('index.singleTicketFollow', {
            url: '/single/ticket/follow',
            templateUrl: 'views/single-ticket-follow.html'
        }).state('index.transitOrder', {
            url: '/transit/order',
            templateUrl: 'views/transit-order.html'
        }).state('index.billInquiry', {
            url: '/bill/inquiry',
            templateUrl: 'views/bill-inquiry.html'
        })
        /*$stateProvider
         .state('import/list', {
         url: '/import/list',
         templateUrl: 'views/import-list.html'
         })
         .state('import/detail', {
         url: '/import/detail',
         templateUrl: 'views/import-detail.html'
         })
         .state('export/list', {
         url: '/export/list',
         templateUrl: 'views/export-list.html'
         })
         .state('export/detail', {
         url: '/export/detail',
         templateUrl: 'views/export-detail.html'
         })
         .state('ticket/follow', {
         url: '/ticket/follow',
         templateUrl: 'views/ticket-follow.html'
         })
         .state('single/ticket/follow', {
         url: '/single/ticket/follow',
         templateUrl: 'views/single-ticket-follow.html'
         })
         .state('transit/order', {
         url: '/transit/order',
         templateUrl: 'views/transit-order.html'
         })
         .state('bill/inquiry', {
         url: '/bill/inquiry',
         templateUrl: 'views/bill-inquiry.html'
         })*/
    }]);