               
                пресет стриминга в mediaPlayer

                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(this@FrequencyRadioService, Uri.parse(stationBroadcastUri))
                }

                FAQ - https://ru.stackoverflow.com/questions/514388/%D0%9F%D1%80%D0%B8-%D0%B2%D0%BE%D1%81%D0%BF%D1%80%D0%BE%D0%B8%D0%B7%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BC%D0%B5%D0%BB%D0%BE%D0%B4%D0%B8%D0%B8-%D0%BE%D1%88%D0%B8%D0%B1%D0%BA%D0%B0-error-38-0