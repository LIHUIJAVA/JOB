var menus = [
    {
        id: "9000",
        text: "header",
        icon: "",
        isHeader: true
    },
    {
        id: "9001",
        text: "采购管理 ",
        icon: "fa fa-laptop",
        //采购管理---二级菜单1 
        children: [
            {
                id: "90011",
                text: "采购订货",
                icon: "fa fa-circle-o",
                //url: "UI/buttons_iframe.html",
                //targetType: "iframe-tab",
                //采购管理----三级菜单1
                children:[
                    {
                        id: "90011-1",
                        text: "采购订单",
                        url: "UI/buttons_iframe.html",
                        targetType: "iframe-tab",
                    },
                    {
                        id: "90011-2",
                        text: "采购订单列表",
                        url: "UI/icons_iframe.html",
                        targetType: "iframe-tab",
                    },
                ]
            },
            {
                id: "90012",
                text: "采购到货",
                //url: "UI/icons_iframe.html",
                //targetType: "iframe-tab",
                icon: "fa fa-circle-o",
               //采购d到货----三级菜单2
               children:[
                    {
                        id: "90012-1",
                        text: "到货单",
                        url: "UI/buttons.html",
                        targetType: "iframe-tab",
                    },
                    {
                        id: "90012-2",
                        text: "到货拒收单",
                        url: "UI/icons_iframe.html",
                        targetType: "iframe-tab",
                    },
                    {
                        id: "90012-3",
                        text: "到货单列表",
                        url: "UI/icons.html",
                        targetType: "iframe-tab",
                    },
                ]
            },
            {
                id: "90013",
                text: "general",
                url: "UI/general_iframe.html",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o"
            },
            {
                id: "90014",
                text: "modals",
                url: "UI/modals_iframe.html",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o"
            },
            {
                id: "90015",
                text: "sliders",
                url: "UI/sliders_iframe.html",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o"
            },
            {
                id: "90016",
                text: "timeline",
                url: "UI/timeline_iframe.html",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o"
            }
        ]
    },
    {
        id: "9002",
        text: "销售管理",
        icon: "fa fa-edit",
        children: [
            {
                id: "90021",
                text: "销售订货",
                //url: "forms/advanced_iframe.html",
                //targetType: "iframe-tab",
                icon: "fa fa-circle-o",
                children:[
                    {
                        id: "90021-1",
                        text: "销售单",
                        url: "forms/advanced_iframe.html",
                        targetType: "iframe-tab",
                    },
                    {
                        id: "90021-2",
                        text: "退货单",
                        url: "forms/icons.html",
                        targetType: "iframe-tab",
                    },
                    {
                        id: "90021-3",
                        text: "销售单列表",
                        url: "forms/sliders_iframe.html",
                        targetType: "iframe-tab",
                    },
                ]
            },
            {
                id: "90022",
                text: "销售出货",
                url: "forms/general_iframe.html",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o"
            },
            {
                id: "90023",
                text: "委托代销",
                url: "forms/editors_iframe.html",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o"
            },
            {
                id: "90024",
                text: "销售报表",
                url: "https://www.baidu.com",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o",
                urlType: 'abosulte'
            }
        ]
    },
    {
        id: "9003",
        text: "库存管理",
        icon: "fa fa-calendar",
        children: [
            {
                id: "90023",
                text: "调拨业务",
                //url: "forms/advanced_iframe.html",
                //targetType: "iframe-tab",
                icon: "fa fa-circle-o",
                children:[
                    {
                        id: "90023-1",
                        text: "调拨单",
                        url: "forms/advanced_iframe.html",
                        targetType: "iframe-tab",
                    },
                    {
                        id: "90023-2",
                        text: "调拨单列表",
                        url: "forms/icons.html",
                        targetType: "iframe-tab",
                    },
                ]
            },
            {
                id: "90024",
                text: "盘点业务",
                url: "forms/general_iframe.html",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o",
                children:[
                    {
                        id: "90024-1",
                        text: "盘点单",
                        url: "forms/advanced_iframe.html",
                        targetType: "iframe-tab",
                    },
                    {
                        id: "90024-2",
                        text: "盘点单列表",
                        url: "forms/icons.html",
                        targetType: "iframe-tab",
                    },
                ]
            },
            {
                id: "90023",
                text: "委托代销",
                url: "forms/editors_iframe.html",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o"
            },
            {
                id: "90024",
                text: "销售报表",
                url: "https://www.baidu.com",
                targetType: "iframe-tab",
                icon: "fa fa-circle-o",
                urlType: 'abosulte'
            }
        ]
    }
];
$('.sidebar-menu').sidebarMenu({data: menus});