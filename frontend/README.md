# Ticketing Frontend

Next.js (App Router, TypeScript, Tailwind CSS) UI for the ticketing app. Talks
to the [backend](../backend/README.md) REST API over plain `fetch`.

## Running locally

```bash
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000). By default it calls the
API at `http://localhost:8808` (see `backend/README.md` for running that).
To point at a different API, copy `.env.example` to `.env.local` and set
`NEXT_PUBLIC_API_URL`.

## Structure

* `src/app/page.tsx` - ticket list
* `src/app/tickets/new/page.tsx` - create ticket form
* `src/app/tickets/[id]/page.tsx` - ticket detail, status update, delete
* `src/lib/api.ts` - fetch wrapper for the backend API
* `src/lib/types.ts` - types mirroring the backend's request/response DTOs
* `src/components/Badges.tsx` - priority/status badges

## Notes

* `NEXT_PUBLIC_API_URL` is inlined into the client bundle **at build time**
  (see `Dockerfile`, which accepts it as a build `ARG`) - it can't be changed
  by setting an environment variable on an already-built container.
* All data fetching happens client-side (`"use client"` pages), so the
  browser calls the API directly - the backend's CORS config
  (`APP_CORS_ALLOWED_ORIGINS`) must allow whatever origin the frontend is
  served from.

## Learn More

- [Next.js Documentation](https://nextjs.org/docs)
- [Learn Next.js](https://nextjs.org/learn)
