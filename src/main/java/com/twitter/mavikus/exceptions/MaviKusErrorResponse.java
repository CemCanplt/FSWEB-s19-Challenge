package com.twitter.mavikus.exceptions;
/*
Not: HttpStatus kodları hakkında:
  200: Başarılı | 201: Başarıyla oluşturuldu
  400: Geçersiz istek | 401: Yetkisiz | 403: Yasaklı | 404: Bulunamadı
  500: Sunucu hatası | 502: Geçersiz yanıt | 503: Hizmet dışı | 504: Zaman aşımı
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MaviKusErrorResponse {
    private int statusCode;
    private String message;
    private Long timestamp;
}
