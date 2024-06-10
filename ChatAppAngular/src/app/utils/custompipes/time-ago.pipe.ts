import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeAgo'
})
export class TimeAgoPipe implements PipeTransform {

  transform(value: any): string {
    const date = new Date(value);
    const now = new Date();

    const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);

    if (seconds < 60) {
      return `${seconds} sec ago`;
    }

    const minutes = Math.floor(seconds / 60);
    if (minutes < 60) {
      return `${minutes} min ago`;
    }

    const hours = Math.floor(minutes / 60);
    if (hours < 24) {
      return `${hours} hours ago`;
    }

    const days = Math.floor(hours / 24);
    if (days < 365) {
      return `${days} days ago`;
    }

    const years = Math.floor(days / 365);
    return `${years} years ago`;
  }


}
