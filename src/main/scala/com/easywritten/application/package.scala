package com.easywritten

import zio.Has

package object application {
  type ItemApplicationService = Has[ItemApplicationService.Service]
}
