package com.easywritten.scrum.domain.model

import zio.Has

package object item {
  type ItemRepository = Has[ItemRepository.Service]
}
