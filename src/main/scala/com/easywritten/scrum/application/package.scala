package com.easywritten.scrum

import zio.Has

package object application {
  type ItemApplicationService = Has[ItemApplicationService.Service]
}
