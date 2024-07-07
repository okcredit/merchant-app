package tech.okcredit.customization

import kotlinx.serialization.json.Json
import tech.okcredit.customization.models.TargetComponent
import kotlin.test.Test

class CustomizationTest {

    @Test
    fun `response parsing test`() {
        val response = """
            [
              {
                "target": "home_banner",
                "component": {
                  "items": [
                    {
                      "event_handlers": {
                        "click": [
                          {
                            "action": "navigate",
                            "url": "https://okcredit.app/merchant/v1/web/https%3A%2F%2Foknivesh.okcredit.in%2Fp2p%2Foffer"
                          },
                          {
                            "action": "track",
                            "event": "Entry Point Clicked",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/p2p_lending_7_feb_24.gif",
                              "event.kind": "cell",
                              "event.title": "Earn Interest Daily",
                              "event.version": "v1alpha",
                              "metadata.feature": "p2p_v1",
                              "metadata.name": "p2p_lending",
                              "source": "Banner",
                              "target": "home_banner",
                              "type": "p2p_lending"
                            }
                          }
                        ],
                        "view": [
                          {
                            "action": "track",
                            "event": "Entry Point Viewed",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/p2p_lending_7_feb_24.gif",
                              "event.kind": "cell",
                              "event.title": "Earn Interest Daily",
                              "event.version": "v1alpha",
                              "metadata.feature": "p2p_v1",
                              "metadata.name": "p2p_lending",
                              "source": "Banner",
                              "target": "home_banner",
                              "type": "p2p_lending"
                            }
                          }
                        ]
                      },
                      "icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/p2p_lending_7_feb_24.gif",
                      "kind": "cell",
                      "metadata": {
                        "feature": "p2p_v1",
                        "name": "p2p_lending"
                      },
                      "title": "Earn Interest Daily",
                      "version": "v1alpha"
                    },
                    {
                      "event_handlers": {
                        "click": [
                          {
                            "action": "navigate",
                            "url": "https://okcredit.app/merchant/v1/gst_bills"
                          },
                          {
                            "action": "track",
                            "event": "Entry Point Clicked",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/gst_bills.png",
                              "event.kind": "cell",
                              "event.title": "Bills",
                              "event.version": "v1alpha",
                              "metadata.feature": "gst_bills",
                              "metadata.name": "gst_bills",
                              "source": "Banner",
                              "target": "home_banner",
                              "type": "gst_bills"
                            }
                          }
                        ],
                        "view": [
                          {
                            "action": "track",
                            "event": "Entry Point Viewed",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/gst_bills.png",
                              "event.kind": "cell",
                              "event.title": "Bills",
                              "event.version": "v1alpha",
                              "metadata.feature": "gst_bills",
                              "metadata.name": "gst_bills",
                              "source": "Banner",
                              "target": "home_banner",
                              "type": "gst_bills"
                            }
                          }
                        ]
                      },
                      "icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/gst_bills.png",
                      "kind": "cell",
                      "metadata": {
                        "feature": "gst_bills",
                        "name": "gst_bills"
                      },
                      "title": "Bills",
                      "version": "v1alpha"
                    },
                    {
                      "event_handlers": {
                        "click": [
                          {
                            "action": "navigate",
                            "url": "https://okcredit.app/merchant/v1/stock_management"
                          },
                          {
                            "action": "track",
                            "event": "Entry Point Clicked",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/stock_management.png",
                              "event.kind": "cell",
                              "event.title": "Stock Management",
                              "event.version": "v1alpha",
                              "metadata.feature": "stock_management",
                              "metadata.name": "stock_management",
                              "source": "Banner",
                              "target": "home_banner",
                              "type": "stock_management"
                            }
                          }
                        ],
                        "view": [
                          {
                            "action": "track",
                            "event": "Entry Point Viewed",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/stock_management.png",
                              "event.kind": "cell",
                              "event.title": "Stock Management",
                              "event.version": "v1alpha",
                              "metadata.feature": "stock_management",
                              "metadata.name": "stock_management",
                              "source": "Banner",
                              "target": "home_banner",
                              "type": "stock_management"
                            }
                          }
                        ]
                      },
                      "icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/stock_management.png",
                      "kind": "cell",
                      "metadata": {
                        "feature": "stock_management",
                        "name": "stock_management"
                      },
                      "title": "Stock Management",
                      "version": "v1alpha"
                    }
                  ],
                  "kind": "grid",
                  "metadata": {
                    "debug": true,
                    "lang": "en",
                    "name": "Grid"
                  },
                  "version": "v1alpha"
                }
              },
              {
                "target": "home_side_navigation",
                "component": {
                  "items": [
                    {
                      "event_handlers": {
                        "click": [
                          {
                            "action": "navigate",
                            "url": "https://okcredit.app/merchant/v1/share"
                          },
                          {
                            "action": "track",
                            "event": "Entry Point Clicked",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/ic_share%404x.webp",
                              "event.kind": "menu_item",
                              "event.title": "Share",
                              "event.version": "v1alpha",
                              "metadata.feature": "referrer_reward",
                              "metadata.name": "Share",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "Share"
                            }
                          }
                        ],
                        "view": [
                          {
                            "action": "track",
                            "event": "Entry Point Viewed",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/ic_share%404x.webp",
                              "event.kind": "menu_item",
                              "event.title": "Share",
                              "event.version": "v1alpha",
                              "metadata.feature": "referrer_reward",
                              "metadata.name": "Share",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "Share"
                            }
                          }
                        ]
                      },
                      "icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/ic_share%404x.webp",
                      "kind": "menu_item",
                      "metadata": {
                        "feature": "referrer_reward",
                        "name": "Share"
                      },
                      "title": "Share",
                      "version": "v1alpha"
                    },
                    {
                      "event_handlers": {
                        "click": [
                          {
                            "action": "navigate",
                            "url": "https://okcredit.app/merchant/v1/web/https%3A%2F%2Floan.okcredit.in%2F"
                          },
                          {
                            "action": "track",
                            "event": "Entry Point Clicked",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/sidebar_okloan.webp",
                              "event.kind": "menu_item",
                              "event.title": "OkLoan",
                              "event.version": "v1alpha",
                              "metadata.feature": "okloan",
                              "metadata.name": "okloan",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "okloan"
                            }
                          }
                        ],
                        "view": [
                          {
                            "action": "track",
                            "event": "Entry Point Viewed",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/sidebar_okloan.webp",
                              "event.kind": "menu_item",
                              "event.title": "OkLoan",
                              "event.version": "v1alpha",
                              "metadata.feature": "okloan",
                              "metadata.name": "okloan",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "okloan"
                            }
                          }
                        ]
                      },
                      "icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/sidebar_okloan.webp",
                      "kind": "menu_item",
                      "metadata": {
                        "feature": "okloan",
                        "name": "okloan"
                      },
                      "title": "OkLoan",
                      "version": "v1alpha"
                    },
                    {
                      "event_handlers": {
                        "click": [
                          {
                            "action": "navigate",
                            "url": "https://okcredit.app/merchant/v1/web/https%3A%2F%2Faccount.okcredit.in%2Fannual-summary-2022"
                          },
                          {
                            "action": "track",
                            "event": "Entry Point Clicked",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/annual_report_20_dec23.png",
                              "event.kind": "menu_item",
                              "event.title": "Reports",
                              "event.version": "v1alpha",
                              "metadata.feature": "annual_report",
                              "metadata.name": "Report",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "Report"
                            }
                          }
                        ],
                        "view": [
                          {
                            "action": "track",
                            "event": "Entry Point Viewed",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/annual_report_20_dec23.png",
                              "event.kind": "menu_item",
                              "event.title": "Reports",
                              "event.version": "v1alpha",
                              "metadata.feature": "annual_report",
                              "metadata.name": "Report",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "Report"
                            }
                          }
                        ]
                      },
                      "icon": "https://storage.googleapis.com/prod__dynamicui__public__dataset/icons/annual_report_20_dec23.png",
                      "kind": "menu_item",
                      "metadata": {
                        "feature": "annual_report",
                        "name": "Report"
                      },
                      "title": "Reports",
                      "version": "v1alpha"
                    },
                    {
                      "event_handlers": {
                        "click": [
                          {
                            "action": "navigate",
                            "url": "https://okcredit.app/merchant/v1/web/https%3A%2F%2Faccount.okcredit.in%2FcheckRating"
                          },
                          {
                            "action": "track",
                            "event": "Entry Point Clicked",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/okc-s3-bk-okcontent/icon-score-menu.webp",
                              "event.kind": "menu_item",
                              "event.title": "Check OkCredit Score",
                              "event.version": "v1alpha",
                              "metadata.feature": "ok_score_sb",
                              "metadata.name": "ok_score_sb",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "ok_score_sb"
                            }
                          }
                        ],
                        "view": [
                          {
                            "action": "track",
                            "event": "Entry Point Viewed",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/okc-s3-bk-okcontent/icon-score-menu.webp",
                              "event.kind": "menu_item",
                              "event.title": "Check OkCredit Score",
                              "event.version": "v1alpha",
                              "metadata.feature": "ok_score_sb",
                              "metadata.name": "ok_score_sb",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "ok_score_sb"
                            }
                          }
                        ]
                      },
                      "icon": "https://storage.googleapis.com/okc-s3-bk-okcontent/icon-score-menu.webp",
                      "kind": "menu_item",
                      "metadata": {
                        "feature": "ok_score_sb",
                        "name": "ok_score_sb"
                      },
                      "title": "Check OkCredit Score",
                      "version": "v1alpha"
                    },
                    {
                      "event_handlers": {
                        "click": [
                          {
                            "action": "navigate",
                            "url": "https://okcredit.app/merchant/v1/web/https%3A%2F%2Faccount.okcredit.in%2FAddDefaulterList%2Fdashboard"
                          },
                          {
                            "action": "track",
                            "event": "Entry Point Clicked",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/okc-s3-bk-okcontent/icon-defaulters-r-menu.webp",
                              "event.kind": "menu_item",
                              "event.title": "Find Defaulter",
                              "event.version": "v1alpha",
                              "metadata.feature": "find_defaulter",
                              "metadata.name": "find_defaulter",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "find_defaulter"
                            }
                          }
                        ],
                        "view": [
                          {
                            "action": "track",
                            "event": "Entry Point Viewed",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/okc-s3-bk-okcontent/icon-defaulters-r-menu.webp",
                              "event.kind": "menu_item",
                              "event.title": "Find Defaulter",
                              "event.version": "v1alpha",
                              "metadata.feature": "find_defaulter",
                              "metadata.name": "find_defaulter",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "find_defaulter"
                            }
                          }
                        ]
                      },
                      "icon": "https://storage.googleapis.com/okc-s3-bk-okcontent/icon-defaulters-r-menu.webp",
                      "kind": "menu_item",
                      "metadata": {
                        "feature": "find_defaulter",
                        "name": "find_defaulter"
                      },
                      "title": "Find Defaulter",
                      "version": "v1alpha"
                    },
                    {
                      "event_handlers": {
                        "click": [
                          {
                            "action": "navigate",
                            "url": "https://okcredit.app/merchant/v1/account/collection/staff_link"
                          },
                          {
                            "action": "track",
                            "event": "Entry Point Clicked",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/okcredit-icons/ic_staff_collection_link.png",
                              "event.kind": "menu_item",
                              "event.title": "Collection List",
                              "event.version": "v1alpha",
                              "metadata.feature": "staff_collection_link",
                              "metadata.name": "staffCollectionLink",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "staffCollectionLink"
                            }
                          }
                        ],
                        "view": [
                          {
                            "action": "track",
                            "event": "Entry Point Viewed",
                            "properties": {
                              "event.icon": "https://storage.googleapis.com/okcredit-icons/ic_staff_collection_link.png",
                              "event.kind": "menu_item",
                              "event.title": "Collection List",
                              "event.version": "v1alpha",
                              "metadata.feature": "staff_collection_link",
                              "metadata.name": "staffCollectionLink",
                              "source": "Menu Item",
                              "target": "home_side_navigation",
                              "type": "staffCollectionLink"
                            }
                          }
                        ]
                      },
                      "icon": "https://storage.googleapis.com/okcredit-icons/ic_staff_collection_link.png",
                      "kind": "menu_item",
                      "metadata": {
                        "feature": "staff_collection_link",
                        "name": "staffCollectionLink"
                      },
                      "title": "Collection List",
                      "version": "v1alpha"
                    }
                  ],
                  "kind": "vertical_list",
                  "metadata": {
                    "name": "VerticalList"
                  },
                  "version": "v1alpha"
                }
              }
            ]
        """.trimIndent()

        val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        val result = json.decodeFromString<List<TargetComponent>>(response)

        println(result.first())
        println(result[1])
    }
}
