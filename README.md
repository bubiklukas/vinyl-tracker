# Vinyl Tracker

An Android app for managing your vinyl record collection. Add records to your collection or wishlist, browse their tracklists, and keep track of when you acquired them.

## Features

- **Collection** — browse and manage records you own
- **Wishlist** — save records you want to buy
- **Detail view** — album art, tracklist, and ownership date
- **Search** — find albums by name and add them directly
- **Move** — promote a record from wishlist to collection

## Tech Stack

| Technology | Purpose |
|---|---|
| Jetpack Compose + Material 3 | UI |
| Navigation3 | Screen navigation |
| Room | Local database |
| Ktor | HTTP client for iTunes API |
| iTunes Search API | Album and track metadata |
| Coil | Image loading |
| Koin | Dependency injection |

## Architecture

MVVM with a repository pattern. UI layer uses ViewModels backed by use cases that read/write to a Room database and fetch metadata from the iTunes API.
